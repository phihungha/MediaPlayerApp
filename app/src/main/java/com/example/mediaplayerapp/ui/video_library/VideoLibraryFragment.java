package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
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
import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;

import java.util.List;


public class VideoLibraryFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "recycler_column_count";

    VideoLibraryViewModel videoLibraryViewModel;
    RecyclerView recyclerViewAllVideos;
    VideoLibraryItemAdapter videoLibraryItemAdapter;
    private FragmentVideoLibraryBinding binding;
    private int mColumnCount = 1;
    private SortArgs sortArgs = SortArgs.VIDEO_DURATION;
    private SortOrder sortOrder = SortOrder.ASC;
    private List<Video> currentVideosList;

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
                return true;
            }
            case R.id.sort_by_name_menu_item: {
                sortArgs = SortArgs.VIDEO_NAME;
                sortOrder = SortOrder.ASC;

                videoLibraryItemAdapter.updateVideoList(currentVideosList, sortArgs, sortOrder);
                videoLibraryItemAdapter.notifyDataSetChanged();
                return true;
            }
            case R.id.sort_by_length_menu_item: {
                sortArgs = SortArgs.VIDEO_DURATION;
                sortOrder = SortOrder.DESC;

                videoLibraryItemAdapter.updateVideoList(currentVideosList, sortArgs, sortOrder);
                videoLibraryItemAdapter.notifyDataSetChanged();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mColumnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);
        }
        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);

        recyclerViewAllVideos = binding.allVideosRecyclerview;
        if (mColumnCount <= 1) {
            recyclerViewAllVideos.setLayoutManager(
                    new LinearLayoutManager(binding.getRoot().getContext()));
        } else {
            recyclerViewAllVideos.setLayoutManager(
                    new GridLayoutManager(binding.getRoot().getContext(), 2));
        }

        videoLibraryItemAdapter = new VideoLibraryItemAdapter();
        recyclerViewAllVideos.setAdapter(videoLibraryItemAdapter);

        videoLibraryViewModel =
                new ViewModelProvider(getActivity()).get(VideoLibraryViewModel.class);

        videoLibraryViewModel.getAllVideos().observe(getActivity(), videoList -> {
            videoLibraryItemAdapter.updateVideoList(videoList, sortArgs, sortOrder);
            currentVideosList = videoList;
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_COLUMN_COUNT,mColumnCount);
    }

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
}