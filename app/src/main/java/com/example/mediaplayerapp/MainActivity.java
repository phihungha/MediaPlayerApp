package com.example.mediaplayerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mediaplayerapp.databinding.ActivityMainBinding;
import com.example.mediaplayerapp.ui.music_player.BottomMusicPlayerComponent;
import com.example.mediaplayerapp.utils.LanguageConfig;
import com.example.mediaplayerapp.utils.SharedPrefs;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    SharedPrefs sharedPreferences;
    private final ActivityResultLauncher<String> permissionRequestLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (!isGranted)
                            showReadExternalStoragePermissionDeniedNotice();
                    }
            );

    @Override
    protected void attachBaseContext(Context newBase) {
        sharedPreferences = new SharedPrefs(newBase);
        String languageCode = sharedPreferences.getLocale();
        Context context = LanguageConfig.changeLanguage(newBase, languageCode);
        super.attachBaseContext(context);
    }

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