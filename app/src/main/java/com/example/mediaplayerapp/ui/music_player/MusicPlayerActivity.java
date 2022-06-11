package com.example.mediaplayerapp.ui.music_player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playback_history.PlaybackHistoryRepository;
import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Music player UI.
 */
public class MusicPlayerActivity extends AppCompatActivity {

    private static final String LOG_TAG = MusicPlayerActivity.class.getSimpleName();
    public static final String SHUFFLE_MODE_ALL_KEY =
            "com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity.SHUFFLE_MODE_ALL_KEY";
    private static final String SEEK_TO_POSITION_KEY =
            "com.example.mediaplayerapp.ui.video_player.MusicPlayerActivity.SEEK_TO_POSITION_KEY";

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MusicPlayerViewModel musicPlayerViewModel;
    private PlaybackHistoryRepository playbackHistoryRepository;

    private MediaBrowserCompat mediaBrowser;

    private final MediaBrowserCompat.ConnectionCallback connectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    Log.i(LOG_TAG, "Connected to music playback service");

                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    MediaControllerCompat mediaController =
                            new MediaControllerCompat(MusicPlayerActivity.this, token);
                    MediaControllerCompat.setMediaController(MusicPlayerActivity.this, mediaController);
                    getMediaControllerCompat().registerCallback(controllerCallback);

                    MediaMetadataCompat metadata = mediaController.getMetadata();
                    PlaybackStateCompat playbackState = mediaController.getPlaybackState();
                    if (metadata != null)
                        controllerCallback.onMetadataChanged(metadata);
                    if (playbackState != null)
                        controllerCallback.onPlaybackStateChanged(playbackState);

                    musicPlayerViewModel.setEnableTransportControls(true);

                    playFromIntent();
                }

                @Override
                public void onConnectionSuspended() {
                    Log.i(LOG_TAG, "Suspended connection to music playback service");
                    musicPlayerViewModel.setEnableTransportControls(false);
                }

                @Override
                public void onConnectionFailed() {
                    Log.i(LOG_TAG, "Connection to music playback service failed");
                    Toast.makeText(MusicPlayerActivity.this,
                            "Music playback service error!",
                                Toast.LENGTH_SHORT).show();
                }
            };

    private final MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    String mediaUriString = metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI);
                    if (mediaUriString != null)
                        resumeLastPosition(Uri.parse(mediaUriString));
                    else
                        return;
                    musicPlayerViewModel.setCurrentMediaMetadata(metadata);
                    Log.d(LOG_TAG, "Media metadata updated");
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    musicPlayerViewModel.setCurrentPlaybackState(state);
                    Log.d(LOG_TAG, "Playback state changed to " + state.getState());
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    musicPlayerViewModel.setCurrentRepeatMode(repeatMode);
                }

                @Override
                public void onShuffleModeChanged(int shuffleMode) {
                    musicPlayerViewModel.setCurrentShuffleMode(shuffleMode);
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

        musicPlayerViewModel = new ViewModelProvider(this).get(MusicPlayerViewModel.class);
        playbackHistoryRepository = new PlaybackHistoryRepository(getApplication());

        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicPlaybackService.class),
                connectionCallback,
                null);
    }

    private void resumeLastPosition(Uri mediaUri) {
        if (!PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean("resume_last_position", false))
            return;

        Disposable disposable = playbackHistoryRepository.getLastPlaybackPosition(mediaUri)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lastPlaybackPosition -> {
                        if (lastPlaybackPosition != -1)
                            askToResumeLastPosition(lastPlaybackPosition);
                    });
        disposables.add(disposable);
    }

    private void askToResumeLastPosition(long lastPlaybackPosition) {
        Snackbar.make(binding.getRoot(), R.string.resume_last_playback_position, 5000)
                .setAction(R.string.yes,
                           view -> getMediaControllerCompat()
                                   .getTransportControls()
                                   .seekTo(lastPlaybackPosition))
                .show();
    }

    /**
     * Sequentially play song(s) specified by an URI with MusicPlayerActivity.
     * @param context Current context
     * @param uri URI of the media item to play
     */
    public static void launchWithUri(Context context, Uri uri) {
        Intent playbackIntent = new Intent(context, MusicPlayerActivity.class);
        playbackIntent.setData(uri);
        context.startActivity(playbackIntent);
    }

    /**
     * Sequentially play song(s) specified by an URI with MusicPlayerActivity
     * and seek to specified position on the first song.
     * @param context Current context
     * @param uri URI of the media item to play
     * @param seekToPosition Position to seek to
     */
    public static void launchWithUriAndSeekTo(Context context, Uri uri, long seekToPosition) {
        Intent playbackIntent = new Intent(context, MusicPlayerActivity.class);
        playbackIntent.setData(uri);
        playbackIntent.putExtra(SEEK_TO_POSITION_KEY, seekToPosition);
        context.startActivity(playbackIntent);
    }

    /**
     * Randomly play song(s) specified by an URI with MusicPlayerActivity.
     * @param context Current context
     * @param uri URI of the media item to play
     */
    public static void launchWithUriAndShuffleAll(Context context, Uri uri) {
        Intent playbackIntent = new Intent(context, MusicPlayerActivity.class);
        playbackIntent.setData(uri);
        playbackIntent.putExtra(SHUFFLE_MODE_ALL_KEY, true);
        context.startActivity(playbackIntent);
    }

    /**
     * Enter edge-to-edge UI mode.
     */
    public void enterEdgeToEdgeUIMode() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        }

        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    /**
     * Exit edge-to-edge UI mode.
     */
    public void exitEdgeToEdgeUIMode() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController != null)
            windowInsetsController.show(WindowInsetsCompat.Type.navigationBars());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.cyan));
    }

    /**
     * Play music from intent's URI if there is one.
     */
    private void playFromIntent() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            getMediaController()
                    .getTransportControls()
                    .playFromUri(uri, null);
            if (uri.getScheme().equals(GetPlaybackUriUtils.PLAYBACK_URI_SCHEME)
                    && uri.getPathSegments().get(0).equals(GetPlaybackUriUtils.PLAYLIST_URI_SEGMENT)) {
                int currentPlaylistId = Integer.parseInt(uri.getPathSegments().get(1));
                musicPlayerViewModel.setCurrentPlaylistId(currentPlaylistId);
            }
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean(MusicPlayerActivity.SHUFFLE_MODE_ALL_KEY))
                getMediaControllerCompat()
                        .getTransportControls()
                        .setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
        }
    }

    private MediaControllerCompat getMediaControllerCompat() {
        return MediaControllerCompat.getMediaController(this);
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