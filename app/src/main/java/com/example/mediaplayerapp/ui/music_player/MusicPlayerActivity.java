package com.example.mediaplayerapp.ui.music_player;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;

import java.util.Objects;

public class MusicPlayerActivity extends AppCompatActivity {

    ActivityMusicPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.musicCloseBtn.setOnClickListener(view -> finish());
        binding.musicSeekbar.setDuration(100);
        binding.musicSeekbar.setPosition(50);

        binding.repeatBtn.setImageLevel(1);
        binding.musicPlayPauseBtn.setOnClickListener(view -> binding.musicPlayPauseBtn.setImageLevel(1));

    }
}