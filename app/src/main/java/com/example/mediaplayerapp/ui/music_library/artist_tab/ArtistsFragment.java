package com.example.mediaplayerapp.ui.music_library.artist_tab;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
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
import com.example.mediaplayerapp.data.music_library.ArtistsRepository;
import com.example.mediaplayerapp.databinding.FragmentArtistsBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.utils.SortOrder;

@SuppressLint("NotifyDataSetChanged")
public class ArtistsFragment extends Fragment {

    private static final String CURRENT_DISPLAY_MODE_KEY = "current_display_mode";
    private static final String CURRENT_SORT_BY_KEY = "current_sort_by";
    private static final String CURRENT_SORT_ORDER_KEY = "current_sort_order";

    private static final int GRID_MODE_COLUMN_NUM = 2;

    private ArtistAdapter artistAdapter;

    private DisplayMode currentDisplayMode;
    private ArtistsRepository.SortBy currentSortBy;
    private SortOrder currentSortOrder;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private ArtistsViewModel viewModel;

    private FragmentArtistsBinding binding;

    public ArtistsFragment() {
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
        binding = FragmentArtistsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ArtistsViewModel.class);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());

        artistAdapter = new ArtistAdapter(getContext());
        binding.artistList.setAdapter(artistAdapter);

        binding.artistsSwipeRefreshContainer.setOnRefreshListener(this::loadAllArtists);
        binding.artistsSwipeRefreshContainer.setColorSchemeResources(R.color.cyan);

        if (savedInstanceState != null)
            restoreInstanceState(savedInstanceState);
        else {
            setSortMode(ArtistsRepository.SortBy.NAME, SortOrder.ASC);
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
        currentSortBy = (ArtistsRepository.SortBy) savedInstanceState.getSerializable(CURRENT_SORT_BY_KEY);
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
        inflater.inflate(R.menu.artist_tab_options_menu, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.artist_tab_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                artistAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                artistAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.artist_tab_show_as_grid)
            setDisplayModeAsGrid();
        else if (item.getItemId() == R.id.artist_tab_show_as_list)
            setDisplayModeAsList();
        else if (item.getItemId() == R.id.artist_tab_sort_by_name_asc)
            setSortMode(ArtistsRepository.SortBy.NAME, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_name_desc)
            setSortMode(ArtistsRepository.SortBy.NAME, SortOrder.DESC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_albums_asc)
            setSortMode(ArtistsRepository.SortBy.NUMBER_OF_ALBUMS, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_albums_desc)
            setSortMode(ArtistsRepository.SortBy.NUMBER_OF_ALBUMS, SortOrder.DESC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_songs_asc)
            setSortMode(ArtistsRepository.SortBy.NUMBER_OF_TRACKS, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_songs_desc)
            setSortMode(ArtistsRepository.SortBy.NUMBER_OF_TRACKS, SortOrder.DESC);

        return true;
    }

    /**
     * Change current sort mode.
     * @param sortBy Sort by what
     * @param sortOrder Sort order
     */
    private void setSortMode(ArtistsRepository.SortBy sortBy, SortOrder sortOrder) {
        currentSortBy = sortBy;
        currentSortOrder = sortOrder;
        loadAllArtists();
    }

    private void loadAllArtists() {
        viewModel.getAllArtists(currentSortBy, currentSortOrder).observe(
                getViewLifecycleOwner(),
                newArtists ->  {
                    artistAdapter.updateArtists(newArtists);
                    binding.artistsSwipeRefreshContainer.setRefreshing(false);
                });
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        if (currentDisplayMode == DisplayMode.GRID)
            return;
        binding.artistList.setLayoutManager(gridLayoutManager);
        artistAdapter.setDisplayMode(DisplayMode.GRID);
        currentDisplayMode = DisplayMode.GRID;
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        if (currentDisplayMode == DisplayMode.LIST)
            return;
        binding.artistList.setLayoutManager(linearLayoutManager);
        artistAdapter.setDisplayMode(DisplayMode.LIST);
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