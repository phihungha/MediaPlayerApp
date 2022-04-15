package com.example.mediaplayerapp.ui.video_library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;

public class VideoLibraryFragment extends Fragment {

    private FragmentVideoLibraryBinding binding;
    VideoLibraryViewModel videoLibraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoLibraryViewModel = new ViewModelProvider(this).get(VideoLibraryViewModel.class);

        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);

        final TextView textView = binding.textVideoLibrary;
        videoLibraryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        String path = Environment.getExternalStorageDirectory().getPath();
        binding.videoPlayButton.setOnClickListener(view -> {
            Intent startPlayback = new Intent(requireActivity(), VideoPlayerActivity.class);
            ArrayList<String> videoUris = new ArrayList<>();
            videoUris.add(Uri.fromFile(new File(path + "/Download/video_sample.mp4")).toString());
            videoUris.add(Uri.fromFile(new File(path + "/Download/video_sample_2.mp4")).toString());
            videoUris.add(Uri.fromFile(new File(path + "/Download/video_sample_3.mp4")).toString());
            startPlayback.putStringArrayListExtra(VideoPlayerActivity.VIDEO_URI_LIST, videoUris);
            startActivity(startPlayback);
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}