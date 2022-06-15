package com.example.mediaplayerapp.ui.video_library;

import android.net.Uri;
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

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.video_library.VideosRepository;
import com.example.mediaplayerapp.databinding.FragmentVideoLibraryBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.SortOrder;

public class VideoLibraryFragment extends Fragment {

    private static final String CURRENT_DISPLAY_MODE_KEY = "current_display_mode";
    private static final String CURRENT_SORT_BY_KEY = "current_sort_by";
    private static final String CURRENT_SORT_ORDER_KEY = "current_sort_order";

    private static final int GRID_MODE_COLUMN_NUM = 2;

    private VideoAdapter videoAdapter;

    private DisplayMode currentDisplayMode;
    private VideosRepository.SortBy currentSortBy;
    private SortOrder currentSortOrder;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private VideoLibraryViewModel viewModel;

    private FragmentVideoLibraryBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoLibraryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(VideoLibraryViewModel.class);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());

        videoAdapter = new VideoAdapter(requireContext(), orderIndex -> {
            Uri playbackUri = GetPlaybackUriUtils.forVideoLibrary(currentSortBy, currentSortOrder, orderIndex);
            VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        });
        binding.videoList.setAdapter(videoAdapter);
        binding.videoList.setHasFixedSize(true);

        binding.videosSwipeRefreshContainer.setOnRefreshListener(
                () -> viewModel.loadAllVideos(currentSortBy, currentSortOrder)
        );
        binding.videosSwipeRefreshContainer.setColorSchemeResources(R.color.cyan);

        viewModel.getAllVideos().observe(
                getViewLifecycleOwner(),
                videos -> {
                    videoAdapter.submitList(videos);
                    binding.videosSwipeRefreshContainer.setRefreshing(false);
                }
        );

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            setSortMode(VideosRepository.SortBy.NAME, SortOrder.ASC);
            currentDisplayMode = DisplayMode.LIST;
            setDisplayModeAsGrid();
        }

        return binding.getRoot();
    }

    /**
     * Restore fragment's last state.
     * @param savedInstanceState Last state
     */
    private void restoreInstanceState(Bundle savedInstanceState) {
        currentDisplayMode = (DisplayMode) savedInstanceState.getSerializable(CURRENT_DISPLAY_MODE_KEY);
        currentSortBy = (VideosRepository.SortBy) savedInstanceState.getSerializable(CURRENT_SORT_BY_KEY);
        currentSortOrder = (SortOrder) savedInstanceState.getSerializable(CURRENT_SORT_ORDER_KEY);

        setSortMode(currentSortBy, currentSortOrder);

        if (currentDisplayMode == DisplayMode.GRID) {
            // Initial value needs to be appropriately set so default display mode
            // can be set using setDisplayMode methods.
            currentDisplayMode = DisplayMode.LIST;
            setDisplayModeAsGrid();
        } else {
            currentDisplayMode = DisplayMode.GRID;
            setDisplayModeAsList();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.video_library_options_menu, menu);

        MenuItem searchOption = menu.findItem(R.id.search_menu_item);
        SearchView searchView = (SearchView) searchOption.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                videoAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.video_library_show_as_list_menu_item) {
            setDisplayModeAsList();
        } else if (itemId == R.id.video_library_show_as_grid_menu_item) {
            setDisplayModeAsGrid();
        } else if (itemId == R.id.video_library_sort_by_title_asc) {
            setSortMode(VideosRepository.SortBy.NAME, SortOrder.ASC);
        } else if (itemId == R.id.video_library_sort_by_title_desc) {
            setSortMode(VideosRepository.SortBy.NAME, SortOrder.DESC);
        } else if (itemId == R.id.video_library_sort_by_duration_asc) {
            setSortMode(VideosRepository.SortBy.DURATION, SortOrder.ASC);
        } else if (itemId == R.id.video_library_sort_by_duration_desc) {
            setSortMode(VideosRepository.SortBy.DURATION, SortOrder.DESC);
        } else if (itemId == R.id.video_library_sort_by_time_added_asc) {
            setSortMode(VideosRepository.SortBy.TIME_ADDED, SortOrder.ASC);
        } else if (itemId == R.id.video_library_sort_by_time_added_desc) {
            setSortMode(VideosRepository.SortBy.TIME_ADDED, SortOrder.DESC);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Change current sort mode.
     * @param sortBy Sort by what
     * @param sortOrder Sort order
     */
    private void setSortMode(VideosRepository.SortBy sortBy, SortOrder sortOrder) {
        viewModel.loadAllVideos(sortBy, sortOrder);
        currentSortBy = sortBy;
        currentSortOrder = sortOrder;
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        if (currentDisplayMode == DisplayMode.GRID)
            return;
        binding.videoList.setLayoutManager(gridLayoutManager);
        videoAdapter.setDisplayMode(DisplayMode.GRID);
        currentDisplayMode = DisplayMode.GRID;
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        if (currentDisplayMode == DisplayMode.LIST)
            return;
        binding.videoList.setLayoutManager(linearLayoutManager);
        videoAdapter.setDisplayMode(DisplayMode.LIST);
        currentDisplayMode = DisplayMode.LIST;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_DISPLAY_MODE_KEY, currentDisplayMode);
        outState.putSerializable(CURRENT_SORT_ORDER_KEY, currentSortOrder);
        outState.putSerializable(CURRENT_SORT_BY_KEY, currentSortBy);
    }
}