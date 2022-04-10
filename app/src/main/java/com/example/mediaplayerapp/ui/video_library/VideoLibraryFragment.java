package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;

import java.util.ArrayList;
import java.util.List;

public class VideoLibraryFragment extends Fragment {

    private FragmentVideoLibraryBinding binding;
    VideoLibraryViewModel videoLibraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoLibraryViewModel = new ViewModelProvider(this).get(VideoLibraryViewModel.class);

        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);

        RecyclerView recyclerViewAllVideos = binding.allVideosRecyclerview;
        List<Video> videos = new ArrayList<>();
        videos.add(new Video(null, "video 1", 343));
        videos.add(new Video(null, "video 2", 343));
        videos.add(new Video(null, "video 3", 343));
        videos.add(new Video(null, "video 4", 343));
        videos.add(new Video(null, "video 5", 343));

        recyclerViewAllVideos.setAdapter(new VideoLibraryItemAdapter(videos));
        recyclerViewAllVideos.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
//        final TextView textView = binding.textVideoLibrary;
//        videoLibraryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}