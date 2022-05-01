package com.example.mediaplayerapp.ui.video_player;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.mediaplayerapp.databinding.ActivityVideoPlayerBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;

import java.util.List;
import java.util.Objects;

public class VideoPlayerActivity extends AppCompatActivity {

    public final static String VIDEO_URI_LIST = "VideoPlayerActivity.VIDEO_URI_LIST";
    private final static String LOG_TAG = VideoPlayerActivity.class.getSimpleName();

    ActivityVideoPlayerBinding binding;

    private ExoPlayer player;
    private MediaSessionCompat mediaSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        enterFullScreenMode();

        player = new ExoPlayer.Builder(this).build();
        binding.videoPlayer.setPlayer(player);

        mediaSession = new MediaSessionCompat(this, LOG_TAG);
        mediaSession.setMediaButtonReceiver(null);
        PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(playbackStateBuilder.build());
        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @NonNull
            @Override
            public MediaDescriptionCompat getMediaDescription(@NonNull Player player, int windowIndex) {
                return new MediaDescriptionCompat.Builder()
                        .setTitle(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title)
                        .setDescription("MediaDescription description for " + windowIndex)
                        .setSubtitle("MediaDescription subtitle")
                        .build();
            }
        });
        mediaSessionConnector.setPlayer(player);

        loadMediaItemsFromIntent(getIntent());
        play();
    }

    /**
     * Load media items from intent into player.
     */
    private void loadMediaItemsFromIntent(Intent intent) {
        List<String> videoUris = intent.getStringArrayListExtra(VIDEO_URI_LIST);
        if (videoUris != null)
            videoUris.forEach(uri -> player.addMediaItem(MediaItem.fromUri(Uri.parse(uri))));
        else
            player.setMediaItem(MediaItem.fromUri(intent.getData()));
    }

    /**
     * Begin playback of loaded media items.
     */
    private void play() {
        player.prepare();
        player.play();
    }

    /**
     * Hide system bars to enter full screen mode.
     */
    private void enterFullScreenMode() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null)
            return;

        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        player.stop();
        player.clearMediaItems();
        loadMediaItemsFromIntent(intent);
        play();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (player.isPlaying())
            enterPictureInPictureMode(new PictureInPictureParams.Builder().build());
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        binding.videoPlayer.setUseController(!isInPictureInPictureMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaSession.setActive(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaSession.setActive(false);
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaSession.release();
        player.release();
    }
}