package com.example.mediaplayerapp.ui.music_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.FragmentMusicLibraryBinding;

public class MusicLibraryFragment extends Fragment {

    private FragmentMusicLibraryBinding binding;
    private MusicLibraryViewModel viewModel;

    public static MusicLibraryFragment newInstance() {
        return new MusicLibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MusicLibraryViewModel.class);
        binding = FragmentMusicLibraryBinding.inflate(inflater, container, false);

        final TextView textView = binding.textMusicLibrary;
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}