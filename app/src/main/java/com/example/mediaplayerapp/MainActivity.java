package com.example.mediaplayerapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.AudioManager;
import android.net.Uri;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mediaplayerapp.databinding.ActivityMainBinding;
import com.example.mediaplayerapp.ui.music_player.BottomMusicPlayerComponent;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> permissionRequestLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (!isGranted)
                            showReadExternalStoragePermissionDeniedNotice();
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        BottomMusicPlayerComponent bottomMusicPlayer = new BottomMusicPlayerComponent(this, binding);
        getLifecycle().addObserver(bottomMusicPlayer);

        checkAndRequestReadExternalStoragePermission();
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

    /**
     * Check if user has already granted READ_EXTERNAL_STORAGE permission.
     * If not request for that permission.
     */
    private void checkAndRequestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            permissionRequestLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * Notices the user that the on-device media files won't be found
     * because READ_EXTERNAL_STORAGE permission is denied.
     */
    private void showReadExternalStoragePermissionDeniedNotice() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.read_external_storage_permission_denied_notice_message)
                .setTitle(R.string.read_external_storage_permission_denied_notice_title)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {});
        dialogBuilder.create().show();
    }
}