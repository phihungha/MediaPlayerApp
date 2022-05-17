package com.example.mediaplayerapp;

import android.app.Activity;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mediaplayerapp.databinding.ActivityMainBinding;
import com.example.mediaplayerapp.ui.music_player.BottomMusicPlayerComponent;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        BottomMusicPlayerComponent bottomMusicPlayer = new BottomMusicPlayerComponent(this, binding);
        getLifecycle().addObserver(bottomMusicPlayer);
    }

    public static void playMusic(Activity activity, Uri uri) {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(activity);
        if (mediaController != null)
            mediaController.getTransportControls().playFromUri(uri, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
}