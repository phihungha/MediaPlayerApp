package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.SongsRepository;
import com.example.mediaplayerapp.databinding.FragmentSongsBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.SortOrder;

@SuppressLint("NotifyDataSetChanged")
public class SongsFragment extends Fragment {

    private static final String CURRENT_DISPLAY_MODE_KEY = "current_display_mode";
    private static final String CURRENT_SORT_BY_KEY = "current_sort_by";
    private static final String CURRENT_SORT_ORDER_KEY = "current_sort_order";

    private static final int GRID_MODE_COLUMN_NUM = 2;

    private SongAdapter songAdapter;

    private DisplayMode currentDisplayMode;
    private SongsRepository.SortBy currentSortBy;
    private SortOrder currentSortOrder;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private SongsViewModel viewModel;

    private FragmentSongsBinding binding;

    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSongsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());

        songAdapter = new SongAdapter(requireContext(), orderIndex -> {
            Uri playbackUri = GetPlaybackUriUtils.forMusicLibrary(currentSortBy, currentSortOrder, orderIndex);
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        });
        binding.songList.setAdapter(songAdapter);
        binding.songList.setHasFixedSize(true);

        binding.songsSwipeRefreshContainer.setOnRefreshListener(
                () -> viewModel.loadAllSongs(currentSortBy, currentSortOrder)
        );
        binding.songsSwipeRefreshContainer.setColorSchemeResources(R.color.cyan);

        viewModel.getAllSongs().observe(
                getViewLifecycleOwner(),
                newSongs ->  {
                    songAdapter.updateSongs(newSongs);
                    binding.songsSwipeRefreshContainer.setRefreshing(false);
        });

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            setSortMode(SongsRepository.SortBy.TITLE, SortOrder.ASC);
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
        currentSortBy = (SongsRepository.SortBy) savedInstanceState.getSerializable(CURRENT_SORT_BY_KEY);
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
        // Fix toolbar item duplication issue on tab changing
        menu.clear();
        inflater.inflate(R.menu.song_tab_options_menu, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.song_tab_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                songAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                songAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.song_tab_show_as_grid)
            setDisplayModeAsGrid();
        else if (item.getItemId() == R.id.song_tab_show_as_list)
            setDisplayModeAsList();
        else if(item.getItemId() == R.id.song_tab_sort_by_title_asc)
            setSortMode(SongsRepository.SortBy.TITLE, SortOrder.ASC);
        else if(item.getItemId() == R.id.song_tab_sort_by_title_desc)
            setSortMode(SongsRepository.SortBy.TITLE, SortOrder.DESC);
        else if(item.getItemId() == R.id.song_tab_sort_by_duration_asc)
            setSortMode(SongsRepository.SortBy.DURATION, SortOrder.ASC);
        else if(item.getItemId() == R.id.song_tab_sort_by_duration_desc)
            setSortMode(SongsRepository.SortBy.DURATION, SortOrder.DESC);
        else if(item.getItemId() == R.id.song_tab_sort_by_time_added_asc)
            setSortMode(SongsRepository.SortBy.TIME_ADDED, SortOrder.ASC);
        else if(item.getItemId() == R.id.song_tab_sort_by_time_added_desc)
            setSortMode(SongsRepository.SortBy.TIME_ADDED, SortOrder.DESC);

        return true;
    }

    /**
     * Change current sort mode.
     * @param sortBy Sort by what
     * @param sortOrder Sort order
     */
    private void setSortMode(SongsRepository.SortBy sortBy, SortOrder sortOrder) {
        viewModel.loadAllSongs(sortBy, sortOrder);
        currentSortBy = sortBy;
        currentSortOrder = sortOrder;
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        if (currentDisplayMode == DisplayMode.GRID)
            return;
        binding.songList.setLayoutManager(gridLayoutManager);
        songAdapter.setDisplayMode(DisplayMode.GRID);
        currentDisplayMode = DisplayMode.GRID;
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        if (currentDisplayMode == DisplayMode.LIST)
            return;
        binding.songList.setLayoutManager(linearLayoutManager);
        songAdapter.setDisplayMode(DisplayMode.LIST);
        currentDisplayMode = DisplayMode.LIST;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_DISPLAY_MODE_KEY, currentDisplayMode);
        outState.putSerializable(CURRENT_SORT_ORDER_KEY, currentSortOrder);
        outState.putSerializable(CURRENT_SORT_BY_KEY, currentSortBy);
    }
}