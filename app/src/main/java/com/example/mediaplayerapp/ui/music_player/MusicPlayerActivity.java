package com.example.mediaplayerapp.ui.music_player;

import android.app.Activity;
import android.content.ComponentName;
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
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;
import com.example.mediaplayerapp.services.MusicPlaybackService;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;

/**
 * Music player UI.
 */
public class MusicPlayerActivity extends AppCompatActivity {

    private static final String LOG_TAG = MusicPlayerControlFragment.class.getSimpleName();
    public static final String SHUFFLE_MODE_ALL_KEY =
            "com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity.SHUFFLE_MODE_ALL_KEY";

    private MusicPlayerViewModel musicPlayerViewModel;

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
                    musicPlayerViewModel.setCurrentMediaMetadata(metadata);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMusicPlayerBinding binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        musicPlayerViewModel = new ViewModelProvider(this).get(MusicPlayerViewModel.class);

        mediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicPlaybackService.class),
                connectionCallback,
                null);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        }
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
        if (extras != null)
            if (extras.getBoolean(MusicPlayerActivity.SHUFFLE_MODE_ALL_KEY))
                getMediaControllerCompat()
                        .getTransportControls()
                        .setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
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