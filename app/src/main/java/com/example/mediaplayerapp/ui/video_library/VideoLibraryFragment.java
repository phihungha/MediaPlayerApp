package com.example.mediaplayerapp.ui.video_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;

import java.util.List;


public class VideoLibraryFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "recycler_column_count";
    public static int recyclerViewColumnCount = 2;
    private FragmentVideoLibraryBinding binding;
    private RecyclerView recyclerViewAllVideos;
    private VideoLibraryItemAdapter videoLibraryItemAdapter;
    private SortArgs sortArgs = SortArgs.VIDEO_NAME;
    private SortOrder sortOrder = SortOrder.ASC;

    private List<Video> currentVideosList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_video_library, menu);

        MenuItem searchOption = menu.findItem(R.id.search_menu_item);
        SearchView searchView = (SearchView) searchOption.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                videoLibraryItemAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.grid_list_change_menu_item) {
            if (recyclerViewColumnCount <= 1) {
                recyclerViewColumnCount = 2;
                recyclerViewAllVideos.setLayoutManager(new GridLayoutManager
                        (binding.getRoot().getContext(), recyclerViewColumnCount));

            } else {
                recyclerViewColumnCount = 1;
                recyclerViewAllVideos.setLayoutManager(new LinearLayoutManager
                        ((binding.getRoot().getContext())));
            }
            recyclerViewAllVideos.setAdapter(videoLibraryItemAdapter);
            return true;

        } else if (itemId == R.id.sort_by_name_menu_item) {
            sortArgs = SortArgs.VIDEO_NAME;
            sortOrder = sortOrder == SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC;

            videoLibraryItemAdapter.updateVideoList(currentVideosList, sortArgs, sortOrder);
            return true;

        } else if (itemId == R.id.sort_by_length_menu_item) {
            sortArgs = SortArgs.VIDEO_DURATION;
            sortOrder = sortOrder == SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC;

            videoLibraryItemAdapter.updateVideoList(currentVideosList, sortArgs, sortOrder);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            recyclerViewColumnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);
        }
        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);
        recyclerViewAllVideos = binding.allVideosRecyclerview;

        if (recyclerViewColumnCount <= 1) {
            recyclerViewAllVideos.setLayoutManager(new LinearLayoutManager
                    (binding.getRoot().getContext()));
        } else {
            recyclerViewAllVideos.setLayoutManager(new GridLayoutManager
                    (binding.getRoot().getContext(), recyclerViewColumnCount));
        }

        videoLibraryItemAdapter = new VideoLibraryItemAdapter(requireActivity());
        recyclerViewAllVideos.setAdapter(videoLibraryItemAdapter);
        recyclerViewAllVideos.setHasFixedSize(true);

        VideoLibraryViewModel videoLibraryViewModel = new ViewModelProvider
                (requireActivity()).get(VideoLibraryViewModel.class);

        videoLibraryViewModel.getAllVideos().observe(requireActivity(), videoList -> {
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
        outState.putInt(ARG_COLUMN_COUNT, recyclerViewColumnCount);
    }

    enum SortArgs {
        VIDEO_NAME,
        VIDEO_DURATION
    }

    enum SortOrder {
        ASC,
        DESC
    }
}