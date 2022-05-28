package com.example.mediaplayerapp.ui.music_player;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.google.android.exoplayer2.ui.TimeBar;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

/**
 * Music player UI.
 */
public class MusicPlayerActivity extends AppCompatActivity {

    private static final String LOG_TAG = MusicPlayerActivity.class.getSimpleName();
    private static final String SHUFFLE_MODE_ALL_KEY =
            "com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity.SHUFFLE_MODE_KEY";
    private static final int AUTOSCROLL_DELAY = 4500;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted)
                            bindAudioVisualizerToAudio();
                        else
                            showAudioVisualizerPermissionDeniedNotice();
                    });

    private MediaBrowserCompat mediaBrowser;
    MediaBrowserCompat.ConnectionCallback connectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    Log.i(LOG_TAG, "Connected to music playback service");
                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    MediaControllerCompat mediaController =
                            new MediaControllerCompat(MusicPlayerActivity.this, token);
                    MediaControllerCompat.setMediaController(MusicPlayerActivity.this, mediaController);
                    setupTransportControls();
                    setupTimeIndicators();
                    MediaMetadataCompat metadata = mediaController.getMetadata();
                    PlaybackStateCompat playbackState = mediaController.getPlaybackState();
                    if (metadata != null)
                        controllerCallback.onMetadataChanged(metadata);
                    if (playbackState != null)
                        controllerCallback.onPlaybackStateChanged(playbackState);
                    playFromIntent();
                }

                @Override
                public void onConnectionSuspended() {
                    Log.i(LOG_TAG, "Suspended connection to music playback service");
                    disableTransportControls();
                }

                @Override
                public void onConnectionFailed() {
                    Log.i(LOG_TAG, "Connection to music playback service failed");
                    Toast.makeText(MusicPlayerActivity.this,
                            "Music playback service error!",
                            Toast.LENGTH_SHORT).show();
                }
            };

    MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    binding.musicPlayerSongTitle.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE));
                    binding.musicPlayerSongArtist.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST));

                    long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
                    binding.musicPlayerSongDuration.setText(MediaTimeUtils.getFormattedTime(duration));
                    binding.musicPlayerSeekbar.setDuration(duration);

                    String artworkUri = metadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
                    Bitmap artworkBitmap = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
                    if (artworkBitmap != null)
                        setArtworkFromBitmap(artworkBitmap);
                    else if (artworkUri != null)
                        setArtworkFromArtworkUri(Uri.parse(artworkUri));
                    else
                        setDefaultArtwork();
                    Log.d(LOG_TAG, "Media metadata views updated");
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                        // Bind visualizer to new audio session id if it changes.
                        if (checkAudioVisualizerPermission())
                            bindAudioVisualizerToAudio();
                        binding.musicPlayerPlayPauseBtn.setImageLevel(1);
                    }
                    else
                        binding.musicPlayerPlayPauseBtn.setImageLevel(0);
                    Log.d(LOG_TAG, "Playback state changed to " + state.getState());
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
                        binding.musicPlayerRepeatBtn.setImageLevel(0);
                    else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
                        binding.musicPlayerRepeatBtn.setImageLevel(1);
                    else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
                        binding.musicPlayerRepeatBtn.setImageLevel(2);
                }

                @Override
                public void onShuffleModeChanged(int shuffleMode) {
                    if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                        binding.musicPlayerShuffleBtn.setImageLevel(0);
                    else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
                        binding.musicPlayerShuffleBtn.setImageLevel(1);
                }

                @Override
                public void onSessionDestroyed() {
                    super.onSessionDestroyed();
                    mediaBrowser.disconnect();
                    Log.i(LOG_TAG, "Music playback service disconnected");
                }
            };

    ActivityMusicPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicPlaybackService.class),
                connectionCallback,
                null);

        binding.musicPlayerCloseBtn.setOnClickListener(view -> finishAfterTransition());
        binding.musicPlayerMenuBtn.setOnClickListener(view -> openMenu());

        binding.musicPlayerVisualizer.setDensity(70);

        // Delay text auto-scroll
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(() -> {
            binding.musicPlayerSongTitle.setSelected(true);
            binding.musicPlayerSongArtist.setSelected(true);
        }, AUTOSCROLL_DELAY);
    }

    /**
     * Sequentially play song(s) specified by an URI with MusicPlayerActivity.
     * @param activity Current activity
     * @param uri URI of the media item to play
     */
    public static void launchWithUri(Activity activity, Uri uri) {
        Intent playbackIntent = new Intent(activity, MusicPlayerActivity.class);
        playbackIntent.setData(uri);
        activity.startActivity(playbackIntent);
    }

    /**
     * Randomly play song(s) specified by an URI with MusicPlayerActivity.
     * @param activity Current activity
     * @param uri URI of the media item to play
     */
    public static void launchWithUriAndShuffleAll(Activity activity, Uri uri) {
        Intent playbackIntent = new Intent(activity, MusicPlayerActivity.class);
        playbackIntent.setData(uri);
        playbackIntent.putExtra(SHUFFLE_MODE_ALL_KEY, true);
        activity.startActivity(playbackIntent);
    }

    /**
     * Play music from intent's URI if there is one.
     */
    private void playFromIntent() {
        Uri uri = getIntent().getData();
        if (uri != null)
            MediaControllerCompat.getMediaController(this)
                    .getTransportControls()
                    .playFromUri(uri, null);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            if (extras.getBoolean(SHUFFLE_MODE_ALL_KEY))
                MediaControllerCompat.getMediaController(this)
                        .getTransportControls()
                        .setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
    }

    /**
     * Open more button's menu.
     */
    private void openMenu() {
        PopupMenu popupMenu = new PopupMenu(this, binding.musicPlayerMenuBtn);
        popupMenu.inflate(R.menu.music_player_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.music_player_add_to_listen_later) {
                // TODO: Add to listen later playlist
            }
            else if (menuItem.getItemId() == R.id.music_player_add_to_favorites) {
                // TODO: Add to favorite playlist
            }
            else if (menuItem.getItemId() == R.id.music_player_playlist) {
                // TODO: Edit current playlist
            }
            return true;
        });
        popupMenu.setForceShowIcon(true);
        popupMenu.show();
    }

    /**
     * Set up transport controls on the UI.
     */
    private void setupTransportControls() {
        binding.musicPlayerPlayPauseBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
            int playbackState = controller.getPlaybackState().getState();
            if (playbackState == PlaybackStateCompat.STATE_PLAYING)
                controller.getTransportControls().pause();
            else if (playbackState == PlaybackStateCompat.STATE_PAUSED)
                controller.getTransportControls().play();
        });

        binding.musicPlayerRepeatBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
            int repeatMode = controller.getRepeatMode();
            if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
                controller.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
            else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
                controller.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
            else if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
                controller.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
        });

        binding.musicPlayerShuffleBtn.setOnClickListener(view -> {
            MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
            int shuffleMode = controller.getShuffleMode();
            if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                controller.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
            else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
                controller.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
        });

        binding.musicPlayerSkipNextBtn.setOnClickListener(
                view -> MediaControllerCompat.getMediaController(this)
                        .getTransportControls()
                        .skipToNext()
        );

        binding.musicPlayerSkipPrevBtn.setOnClickListener(
                view -> MediaControllerCompat.getMediaController(this)
                        .getTransportControls()
                        .skipToPrevious()
        );

        binding.musicPlayerSeekbar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {

            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                timeBar.setPosition(position);
                binding.musicPlayerSongCurrentPosition.setText(MediaTimeUtils.getFormattedTime(position));
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                MediaControllerCompat.getMediaController(MusicPlayerActivity.this)
                        .getTransportControls()
                        .seekTo(position);
            }
        });

        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        mediaController.registerCallback(controllerCallback);
    }

    /**
     * Disable all transport controls on the UI in case
     * we lost connection to the music playback service.
     */
    private void disableTransportControls() {
        binding.musicPlayerPlayPauseBtn.setEnabled(false);
        binding.musicPlayerSkipPrevBtn.setEnabled(false);
        binding.musicPlayerSkipNextBtn.setEnabled(false);
        binding.musicPlayerRepeatBtn.setEnabled(false);
        binding.musicPlayerShuffleBtn.setEnabled(false);
        binding.musicPlayerSeekbar.setEnabled(false);
    }

    /**
     * Begin updating current playback position and seekbar progress.
     */
    private void setupTimeIndicators() {
        Handler handler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MediaControllerCompat controller = MediaControllerCompat.getMediaController(MusicPlayerActivity.this);
                long currentPlaybackPosition = controller.getPlaybackState().getPosition();
                binding.musicPlayerSeekbar.setPosition(currentPlaybackPosition);
                binding.musicPlayerSongCurrentPosition.setText(MediaTimeUtils.getFormattedTime(currentPlaybackPosition));
                handler.postDelayed(this, 100);
            }
        });
    }

    /**
     * Set song's artwork from artwork's uri.
     * @param artworkUri Artwork's uri
     */
    private void setArtworkFromArtworkUri(Uri artworkUri) {
        Glide.with(this)
                .asBitmap()
                .load(artworkUri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setArtworkFromBitmap(resource);
                        Log.d(LOG_TAG, "Song's artwork loaded from uri");
                        setViewsColors(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * Set song's artwork from artwork's bitmap.
     * @param artworkBitmap Artwork's bitmap
     */
    private void setArtworkFromBitmap(Bitmap artworkBitmap) {
        binding.musicPlayerSongArtwork.setImageBitmap(artworkBitmap);

        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new BlurTransformation(6, 5),
                new BrightnessFilterTransformation(-0.1f));
        Glide.with(this)
                .load(artworkBitmap)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(binding.musicPlayerSongArtworkBackground);
        Log.d(LOG_TAG, "Loaded song's artwork from bitmap");

        setViewsColors(artworkBitmap);
    }

    /**
     * Set default artwork.
     */
    private void setDefaultArtwork() {
        binding.musicPlayerSongArtwork.setImageDrawable(null);
        binding.musicPlayerSongArtworkBackground.setImageDrawable(
                ContextCompat.getDrawable(this,
                    R.drawable.shape_default_artwork_background));
        Log.d(LOG_TAG, "Loaded default song's artwork");
        setDefaultViewsColors();
    }

    /**
     * Set colors of views on the UI based on song's artwork.
     * @param artworkBitmap Artwork's bitmap
     */
    private void setViewsColors(Bitmap artworkBitmap) {
        Palette.from(artworkBitmap).generate(palette -> {
            if (palette == null)
                return;
            int color = palette.getLightVibrantColor(
                    ContextCompat.getColor(this, R.color.white));
            int darkerColor = palette.getDarkMutedColor(
                    ContextCompat.getColor(this, R.color.bright_grey));
            binding.musicPlayerSongTitle.setTextColor(color);
            binding.musicPlayerSongArtist.setTextColor(color);
            binding.musicPlayerRepeatBtn.setColorFilter(color);
            binding.musicPlayerShuffleBtn.setColorFilter(color);
            binding.musicPlayerSongDuration.setTextColor(color);
            binding.musicPlayerSongCurrentPosition.setTextColor(color);
            binding.musicPlayerPlayPauseBtn.setColorFilter(color);
            binding.musicPlayerSkipNextBtn.setColorFilter(color);
            binding.musicPlayerSkipPrevBtn.setColorFilter(color);
            binding.musicPlayerSeekbar.setPlayedColor(color);
            binding.musicPlayerSeekbar.setScrubberColor(color);
            binding.musicPlayerSeekbar.setUnplayedColor(darkerColor);
            binding.musicPlayerScreenTitle.setTextColor(color);
            binding.musicPlayerMenuBtn.setColorFilter(color);
            binding.musicPlayerCloseBtn.setColorFilter(color);
            binding.musicPlayerVisualizer.setColor(color);
            Log.d(LOG_TAG, "Updated views' colors using song artwork's palette");
        });
    }

    /**
     * Set views' colors back to default values.
     */
    private void setDefaultViewsColors() {
        int color = ContextCompat.getColor(this, R.color.white);
        int darkerColor = ContextCompat.getColor(this, R.color.bright_grey);
        binding.musicPlayerSongTitle.setTextColor(color);
        binding.musicPlayerSongArtist.setTextColor(color);
        binding.musicPlayerRepeatBtn.setColorFilter(color);
        binding.musicPlayerShuffleBtn.setColorFilter(color);
        binding.musicPlayerSongDuration.setTextColor(color);
        binding.musicPlayerSongCurrentPosition.setTextColor(color);
        binding.musicPlayerPlayPauseBtn.setColorFilter(color);
        binding.musicPlayerSkipNextBtn.setColorFilter(color);
        binding.musicPlayerSkipPrevBtn.setColorFilter(color);
        binding.musicPlayerSeekbar.setPlayedColor(color);
        binding.musicPlayerSeekbar.setScrubberColor(color);
        binding.musicPlayerSeekbar.setUnplayedColor(darkerColor);
        binding.musicPlayerScreenTitle.setTextColor(color);
        binding.musicPlayerMenuBtn.setColorFilter(color);
        binding.musicPlayerCloseBtn.setColorFilter(color);
        binding.musicPlayerVisualizer.setColor(color);
        Log.d(LOG_TAG, "Updated views' colors to default");
    }

    /**
     * Connect audio visualizer to the current music audio stream.
     */
    private void bindAudioVisualizerToAudio() {
        int audioSessionId = MediaControllerCompat
                .getMediaController(this)
                .getExtras()
                .getInt(MusicPlaybackService.AUDIO_SESSION_ID_KEY);
        binding.musicPlayerVisualizer.setPlayer(audioSessionId);
    }

    /**
     * Check RECORD_AUDIO permission for audio visualizer
     * and request it if necessary.
     * @return True if RECORD_AUDIO permission is granted.
     */
    private boolean checkAudioVisualizerPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)
            return true;

        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(R.string.audio_visualizer_permission_rationale_message)
                    .setTitle(R.string.audio_visualizer_permission_rationale_title)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {});
            dialogBuilder.create().show();
        }
        else
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);

        return false;
    }

    /**
     * Show that the audio visualizer has been disabled because
     * RECORD_AUDIO permission is denied.
     */
    private void showAudioVisualizerPermissionDeniedNotice() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.audio_visualizer_permission_denied_notice_message)
                .setTitle(R.string.audio_visualizer_permission_denied_notice_title)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {});
        dialogBuilder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MediaControllerCompat.getMediaController(this) != null)
            MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback);
        mediaBrowser.disconnect();
    }
}