package com.example.mediaplayerapp.ui.music_player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.FragmentMusicPlayerBinding;

public class MusicPlayerFragment extends Fragment {

    private FragmentMusicPlayerBinding binding;
    private MusicPlayerViewModel viewModel;

    public static MusicPlayerFragment newInstance() {
        return new MusicPlayerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MusicPlayerViewModel.class);
        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false);

        viewModel.getText().observe(getViewLifecycleOwner(), i -> {});

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}