package com.example.mediaplayerapp.ui.music_player;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;

/**
 * Music player UI.
 */
public class MusicPlayerActivity extends AppCompatActivity {
    public static final String SHUFFLE_MODE_ALL_KEY =
            "com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity.SHUFFLE_MODE_ALL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMusicPlayerBinding binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
}