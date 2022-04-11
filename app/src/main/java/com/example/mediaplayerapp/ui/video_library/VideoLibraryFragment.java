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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;

import java.util.ArrayList;
import java.util.List;

public class VideoLibraryFragment extends Fragment {

    private FragmentVideoLibraryBinding binding;
    VideoLibraryViewModel videoLibraryViewModel;

    private int mColumnCount = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoLibraryViewModel = new ViewModelProvider(this).get(VideoLibraryViewModel.class);

        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        List<Video> videos = new ArrayList<>();
        videos.add(new Video(null, "video 1", 343));
        videos.add(new Video(null, "video 2", 343));
        videos.add(new Video(null, "video 3", 343));
        videos.add(new Video(null, "video 4", 343));
        videos.add(new Video(null, "video 5", 343));

        RecyclerView recyclerViewAllVideos = binding.allVideosRecyclerview;
        if (mColumnCount <= 1) {
            recyclerViewAllVideos.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        } else {
            recyclerViewAllVideos.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        }
        recyclerViewAllVideos.setAdapter(new VideoLibraryItemAdapter(videos));

//        final TextView textView = binding.textVideoLibrary;
//        videoLibraryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}