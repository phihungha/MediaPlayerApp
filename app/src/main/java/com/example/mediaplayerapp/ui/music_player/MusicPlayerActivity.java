package com.example.mediaplayerapp.ui.music_player;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediaplayerapp.databinding.ActivityMusicPlayerBinding;

public class MusicPlayerActivity extends AppCompatActivity {

    ActivityMusicPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.musicCloseBtn.setOnClickListener(view -> finish());

        binding.musicPlayPauseBtn.setOnClickListener(view -> binding.musicPlayPauseBtn.setImageLevel(1));

    }
}