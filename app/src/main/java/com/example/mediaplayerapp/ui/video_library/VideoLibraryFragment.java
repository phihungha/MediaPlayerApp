package com.example.mediaplayerapp.ui.video_library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.Collections;

public class VideoLibraryFragment extends Fragment {

    private FragmentVideoLibraryBinding binding;
    VideoLibraryViewModel videoLibraryViewModel;
    RecyclerView recyclerViewAllVideos;
    VideoLibraryItemAdapter videoLibraryItemAdapter;
    private int mColumnCount = 1;
    private SortArgs sortArgs = SortArgs.NONE;
    private SortOrder sortOrder = SortOrder.NONE;

    enum SortArgs {
        NONE,
        VIDEO_NAME,
        VIDEO_DURATION
    }

    enum SortOrder {
        NONE,
        ASC,
        DESC
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.test_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grid_list_change_menu_item: {
                if (mColumnCount <= 1) {
                    mColumnCount = 2;
                    recyclerViewAllVideos.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), mColumnCount));

                } else {
                    mColumnCount = 1;
                    recyclerViewAllVideos.setLayoutManager(new LinearLayoutManager((binding.getRoot().getContext())));
                }
                // navigate to settings screen
                return true;
            }
            case R.id.sort_by_length_menu_item: {
                sortArgs = SortArgs.VIDEO_NAME;
                sortOrder = SortOrder.ASC;
                videoLibraryItemAdapter.notifyDataSetChanged();
                // save profile changes
                return true;
            }
            case R.id.sort_by_name_menu_item: {
                sortArgs = SortArgs.VIDEO_DURATION;
                sortOrder = SortOrder.DESC;
                videoLibraryItemAdapter.notifyDataSetChanged();
                // save profile changes
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        recyclerViewAllVideos = binding.allVideosRecyclerview;
        if (mColumnCount <= 1) {
            recyclerViewAllVideos.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        } else {
            recyclerViewAllVideos.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        }
         videoLibraryItemAdapter = new VideoLibraryItemAdapter();
        recyclerViewAllVideos.setAdapter(videoLibraryItemAdapter);

        videoLibraryViewModel =
                new ViewModelProvider(getActivity()).get(VideoLibraryViewModel.class);
        videoLibraryViewModel.getAllVideos().observe(getActivity(), videoList -> {
                videoLibraryItemAdapter.updateVideoList(videoList,sortArgs,sortOrder);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}