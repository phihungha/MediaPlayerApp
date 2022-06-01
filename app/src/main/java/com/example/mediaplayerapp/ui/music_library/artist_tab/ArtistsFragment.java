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
import com.example.mediaplayerapp.databinding.FragmentArtistBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;
import com.example.mediaplayerapp.utils.SortOrder;

@SuppressLint("NotifyDataSetChanged")
public class ArtistsFragment extends Fragment {

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;

    private ArtistAdapter artistAdapter;

    private DisplayMode currentDisplayMode;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private ArtistsViewModel viewModel;

    private FragmentArtistBinding binding;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(this).get(ArtistsViewModel.class);

        setHasOptionsMenu(true);

        artistAdapter = new ArtistAdapter(getContext());
        binding.artistList.setAdapter(artistAdapter);

        viewModel.getAllArtists().observe(getViewLifecycleOwner(), newArtists -> artistAdapter.updateArtists(newArtists));
        viewModel.loadAllArtists(ArtistsRepository.SortBy.NAME, SortOrder.ASC);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        // Initial value needs to be LIST so display mode can change
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
            viewModel.loadAllArtists(ArtistsRepository.SortBy.NAME, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_name_desc)
            viewModel.loadAllArtists(ArtistsRepository.SortBy.NAME, SortOrder.DESC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_albums_asc)
            viewModel.loadAllArtists(ArtistsRepository.SortBy.NUMBER_OF_ALBUMS, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_albums_desc)
            viewModel.loadAllArtists(ArtistsRepository.SortBy.NUMBER_OF_ALBUMS, SortOrder.DESC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_songs_asc)
            viewModel.loadAllArtists(ArtistsRepository.SortBy.NUMBER_OF_TRACKS, SortOrder.ASC);
        else if (item.getItemId() == R.id.artist_tab_sort_by_number_of_songs_desc)
            viewModel.loadAllArtists(ArtistsRepository.SortBy.NUMBER_OF_TRACKS, SortOrder.DESC);

        return true;
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