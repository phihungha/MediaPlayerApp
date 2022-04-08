package com.example.mediaplayerapp.ui.video_player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.FragmentVideoPlayerBinding;

public class VideoPlayerFragment extends Fragment {

    private FragmentVideoPlayerBinding binding;
    private VideoPlayerViewModel viewModel;

    public static VideoPlayerFragment newInstance() {
        return new VideoPlayerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(VideoPlayerViewModel.class);
        binding = FragmentVideoPlayerBinding.inflate(inflater, container, false);

        viewModel.getText().observe(getViewLifecycleOwner(), i -> {});

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}