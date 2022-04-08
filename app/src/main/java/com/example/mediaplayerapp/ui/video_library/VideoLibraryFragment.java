package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;

public class VideoLibraryFragment extends Fragment {

    private FragmentVideoLibraryBinding binding;
    VideoLibraryViewModel videoLibraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoLibraryViewModel = new ViewModelProvider(this).get(VideoLibraryViewModel.class);

        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);

        final TextView textView = binding.textVideoLibrary;
        videoLibraryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}