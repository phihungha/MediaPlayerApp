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
import com.example.mediaplayerapp.databinding.FragmentArtistBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;

@SuppressLint("NotifyDataSetChanged")
public class ArtistsFragment extends Fragment {

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;

    private ArtistAdapter artistAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private DisplayMode currentDisplayMode = DisplayMode.GRID;

    private FragmentArtistBinding binding;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistBinding.inflate(getLayoutInflater(), container, false);
        ArtistsViewModel viewModel = new ViewModelProvider(this).get(ArtistsViewModel.class);

        setHasOptionsMenu(true);

        artistAdapter = new ArtistAdapter(getContext());
        binding.artistList.setAdapter(artistAdapter);

        viewModel.getAllArtists().observe(getViewLifecycleOwner(), newArtists -> artistAdapter.updateSongs(newArtists));

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        setDisplayModeAsGrid(); // Default display mode is grid

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.music_library_options_menu, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

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
        if (item.getItemId() == R.id.change_display_mode)
            changeDisplayMode();

        return true;
    }

    /**
     * Change display mode of the list.
     */
    private void changeDisplayMode() {
        if (currentDisplayMode == DisplayMode.LIST) {
            setDisplayModeAsGrid();
            currentDisplayMode = DisplayMode.GRID;
        } else {
            setDisplayModeAsList();
            currentDisplayMode = DisplayMode.LIST;
        }
        artistAdapter.notifyDataSetChanged();
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        binding.artistList.setLayoutManager(gridLayoutManager);
        binding.artistList.addItemDecoration(
                new GridSpacingItemDecoration(GRID_MODE_COLUMN_NUM,
                        GRID_MODE_SPACING,
                        true));
        artistAdapter.setDisplayMode(DisplayMode.GRID);
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        binding.artistList.setLayoutManager(linearLayoutManager);
        binding.artistList.removeItemDecorationAt(0);
        artistAdapter.setDisplayMode(DisplayMode.LIST);
    }
}