package com.example.mediaplayerapp.ui.music_library.album_tab;

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
import com.example.mediaplayerapp.databinding.FragmentAlbumBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;

@SuppressLint("NotifyDataSetChanged")
public class AlbumsFragment extends Fragment {
    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;
    private SortOrder sortOrder= SortOrder.ASC;
    private AlbumAdapter albumAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private DisplayMode currentDisplayMode = DisplayMode.GRID;
    private AlbumsViewModel viewModel;
    private FragmentAlbumBinding binding;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AlbumsViewModel.class);

        setHasOptionsMenu(true);

        albumAdapter = new AlbumAdapter(requireContext());
        binding.albumList.setAdapter(albumAdapter);

        viewModel.getAllAlbums().observe(getViewLifecycleOwner(), newAlbums -> albumAdapter.updateAlbums(newAlbums));

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        setDisplayModeAsGrid(); // Default display mode is grid

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.music_library_options_menu, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.music_library_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                albumAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                albumAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.music_library_sort_by_title_asc)
            setSortOrderAsASC();
        else if(item.getItemId()== R.id.music_library_sort_by_title_desc)
            setSortOrderAsDESC();
        return true;
    }

    /**
     * Change display mode of the list.
     */
    private void changeDisplayMode(MenuItem item) {
        if (currentDisplayMode == DisplayMode.LIST) {
            setDisplayModeAsGrid();
            item.setIcon(R.drawable.ic_grid_24dp);
            currentDisplayMode = DisplayMode.GRID;
        } else {
            setDisplayModeAsList();
            item.setIcon(R.drawable.ic_list_24dp);
            currentDisplayMode = DisplayMode.LIST;
        }
        albumAdapter.notifyDataSetChanged();
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        binding.albumList.setLayoutManager(gridLayoutManager);
        binding.albumList.addItemDecoration(
                new GridSpacingItemDecoration(GRID_MODE_COLUMN_NUM,
                        GRID_MODE_SPACING,
                        true));
        albumAdapter.setDisplayMode(DisplayMode.GRID);
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        binding.albumList.setLayoutManager(linearLayoutManager);
        binding.albumList.removeItemDecorationAt(0);
        albumAdapter.setDisplayMode(DisplayMode.LIST);
    }

    private void setSortOrderAsASC() {
        viewModel.getAlbumSortByNameASC().observe(getViewLifecycleOwner(), newAlbums -> albumAdapter.updateAlbums(newAlbums));
        sortOrder= SortOrder.ASC;
    }

    private void setSortOrderAsDESC() {
        viewModel.getAlbumSortByNameDESC().observe(getViewLifecycleOwner(), newAlbums -> albumAdapter.updateAlbums(newAlbums));
        sortOrder= SortOrder.DESC;
    }

    enum SortOrder {
        ASC,
        DESC
    }
}