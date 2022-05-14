package com.example.mediaplayerapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mediaplayerapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final String[] mPermissionNeeded = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ActivityMainBinding binding;
    private ActivityResultLauncher<String[]> mPermissionResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_playlist, R.id.navigation_video_library, R.id.navigation_music_library)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    if (null != result.get(mPermissionNeeded)) {
                        checkAndRequestPermission(mPermissionNeeded);
                    }
                }
        );
        checkAndRequestPermission(mPermissionNeeded);
    }

    /**
     * Check if user has already granted permission. If not request for that permission.
     *
     * @param permission The permission that needs checking and requesting
     */
    private void checkAndRequestPermission(String[] permission) {
        int readStoragePermissionResult = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE);

        int writeStoragePermissionResult = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (readStoragePermissionResult != PackageManager.PERMISSION_GRANTED ||
                writeStoragePermissionResult != PackageManager.PERMISSION_GRANTED) {
            mPermissionResultLauncher.launch(permission);
        }
    }
}