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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.ArtistsRepository;
import com.example.mediaplayerapp.databinding.FragmentArtistsBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;
import com.example.mediaplayerapp.utils.SortOrder;

@SuppressLint("NotifyDataSetChanged")
public class ArtistsFragment extends Fragment {

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ArtistsViewModel.class);

        setHasOptionsMenu(true);

        binding.artistsSwipeRefreshContainer.setOnRefreshListener(
                () -> viewModel.loadAllArtists(currentSortBy, currentSortOrder)
        );
        binding.artistsSwipeRefreshContainer.setColorSchemeResources(R.color.cyan);

        artistAdapter = new ArtistAdapter(getContext());
        binding.artistList.setAdapter(artistAdapter);

        viewModel.getAllArtists().observe(getViewLifecycleOwner(),
                newArtists ->  {
                    artistAdapter.updateArtists(newArtists);
                    binding.artistsSwipeRefreshContainer.setRefreshing(false);
                });
        changeSortMode(ArtistsRepository.SortBy.NAME, SortOrder.ASC);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        // Initial value needs to be LIST so default display mode
        //  can be set using setDisplayModeAsGrid()
        currentDisplayMode = DisplayMode.LIST;
        // Default display mode is grid
        setDisplayModeAsGrid();

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
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
            changeSortMode(ArtistsRepository.SortBy.NAME, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_name_desc)
            changeSortMode(ArtistsRepository.SortBy.NAME, SortOrder.DESC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_albums_asc)
            changeSortMode(ArtistsRepository.SortBy.NUMBER_OF_ALBUMS, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_albums_desc)
            changeSortMode(ArtistsRepository.SortBy.NUMBER_OF_ALBUMS, SortOrder.DESC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_songs_asc)
            changeSortMode(ArtistsRepository.SortBy.NUMBER_OF_TRACKS, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_songs_desc)
            changeSortMode(ArtistsRepository.SortBy.NUMBER_OF_TRACKS, SortOrder.DESC);

        return true;
    }

    /**
     * Change current sort mode.
     * @param sortBy Sort by what
     * @param sortOrder Sort order
     */
    private void changeSortMode(ArtistsRepository.SortBy sortBy, SortOrder sortOrder) {
        viewModel.loadAllArtists(sortBy, sortOrder);
        currentSortBy = sortBy;
        currentSortOrder = sortOrder;
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        if (currentDisplayMode == DisplayMode.GRID)
            return;

        binding.artistList.setLayoutManager(gridLayoutManager);
        binding.artistList.addItemDecoration(
                new GridSpacingItemDecoration(GRID_MODE_COLUMN_NUM,
                        GRID_MODE_SPACING,
                        true));

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
        binding.artistList.removeItemDecorationAt(0);

        artistAdapter.setDisplayMode(DisplayMode.LIST);
        currentDisplayMode = DisplayMode.LIST;
    }
}