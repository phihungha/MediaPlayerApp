package com.example.mediaplayerapp.ui.video_player;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.mediaplayerapp.databinding.ActivityVideoPlayerBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    public final static String VIDEO_URI_LIST = "VideoPlayerActivity.VIDEO_URI_LIST";
    private final static String PLAYBACK_POSITION = "PLAYBACK_POSITION";
    private final static String CURRENT_MEDIA_ITEM_INDEX = "CURRENT_MEDIA_ITEM_INDEX";

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVideoPlayerBinding binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        enterFullScreenMode();

        player = new ExoPlayer.Builder(this).build();
        binding.videoPlayer.setPlayer(player);

        if (savedInstanceState != null)
            startPlayback(savedInstanceState.getLong(PLAYBACK_POSITION),
                    savedInstanceState.getInt(CURRENT_MEDIA_ITEM_INDEX));
        else
            startPlayback(0, 0);
    }

    /**
     * Begin video playback from intent's video URI(s).
     * @param startTime Time position to start from
     * @param startMediaItemIndex Media item position to start from if there are many media items
     */
    private void startPlayback(long startTime, int startMediaItemIndex) {
        Intent intent = getIntent();
        List<String> videoUris = intent.getStringArrayListExtra(VIDEO_URI_LIST);
        if (videoUris != null)
            videoUris.forEach(uri -> player.addMediaItem(MediaItem.fromUri(Uri.parse(uri))));
        else
            player.setMediaItem(MediaItem.fromUri(intent.getData()));
        player.seekTo(startMediaItemIndex, startTime);
        player.prepare();
        player.play();
    }

    /**
     * Hide system and action bars to enter full screen mode.
     */
    private void enterFullScreenMode() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_MEDIA_ITEM_INDEX, player.getCurrentMediaItemIndex());
        outState.putLong(PLAYBACK_POSITION, player.getCurrentPosition());
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}