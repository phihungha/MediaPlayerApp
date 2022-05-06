package com.example.mediaplayerapp.ui.music_player;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;
import com.google.android.exoplayer2.ui.TimeBar;

import java.io.File;
import java.util.Locale;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String PLAYBACK_TIME_FORMAT = "%02d:%02d";

    private MediaBrowserCompat mediaBrowser;
    MediaBrowserCompat.ConnectionCallback connectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    MediaControllerCompat mediaController =
                            new MediaControllerCompat(MusicPlayerActivity.this, token);
                    MediaControllerCompat.setMediaController(MusicPlayerActivity.this, mediaController);
                    setupTransportControls();
                    setupTimeIndicators();
                    playSample();
                }

                @Override
                public void onConnectionSuspended() {
                    disableTransportControls();
                }

                @Override
                public void onConnectionFailed() {
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
                    binding.musicPlayerSongArtist.setText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST));

                    long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
                    binding.musicPlayerSongDuration.setText(getFormattedPlaybackPosition(duration));
                    binding.musicPlayerSeekbar.setDuration(duration);

                    String artUri = metadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI);
                    if (artUri != null) {
                        Glide.with(MusicPlayerActivity.this)
                                .asBitmap()
                                .load(Uri.parse(artUri))
                                .into(binding.musicPlayerSongArtwork);
                        Glide.with(MusicPlayerActivity.this)
                                .asBitmap()
                                .load(Uri.parse(artUri))
                                .into(binding.musicPlayerSongArtworkBackground);
                    }
                    Bitmap artBitmap = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
                    if (artBitmap != null) {
                        binding.musicPlayerSongArtwork.setImageBitmap(artBitmap);
                        binding.musicPlayerSongArtworkBackground.setImageBitmap(artBitmap);
                    }
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    if (state.getState() == PlaybackStateCompat.STATE_PLAYING)
                        binding.musicPlayerPlayPauseBtn.setImageLevel(1);
                    else
                        binding.musicPlayerPlayPauseBtn.setImageLevel(0);
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
                }
            };

    ActivityMusicPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicPlaybackService.class),
                connectionCallback,
                null);

        binding.musicPlayerCloseBtn.setOnClickListener(view -> finish());
        binding.musicPlayerMenuBtn.setOnClickListener(view -> openMenu());
    }

    private void playSample() {
        String path = Environment.getExternalStorageDirectory().getPath();
        MediaControllerCompat.getMediaController(this)
                .getTransportControls()
                .playFromUri(Uri.fromFile(new File(path + "/Download/music_sample.flac")), null);
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
                binding.musicPlayerSongCurrentPosition.setText(getFormattedPlaybackPosition(position));
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
                binding.musicPlayerSongCurrentPosition.setText(getFormattedPlaybackPosition(currentPlaybackPosition));
                handler.postDelayed(this, 100);
            }
        });
    }

    /**
     * Get formatted playback position from a milisecond value for display.
     * @param position Playback position as miliseconds
     * @return String Formatted playback position
     */
    private String getFormattedPlaybackPosition(long position) {
        long playedSeconds = position / 1000;
        return String.format(Locale.getDefault(), PLAYBACK_TIME_FORMAT,
                (playedSeconds % 3600) / 60, playedSeconds % 60);
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