package com.example.mediaplayerapp.ui.music_library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.FragmentMusicLibraryBinding;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;

import java.io.File;

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

        File externalStoragePath = Environment.getExternalStorageDirectory();
        Uri uri = Uri.fromFile(new File(externalStoragePath, "/Download/music_sample_2.mp3"));
        Intent intent = new Intent(requireActivity(), MusicPlayerActivity.class);
        intent.setData(uri);
        binding.playMusicBtn.setOnClickListener(view -> startActivity(intent));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}