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
import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.databinding.FragmentAlbumBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;
import com.example.mediaplayerapp.utils.SortOrder;

@SuppressLint("NotifyDataSetChanged")
public class AlbumsFragment extends Fragment {
    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;
    private AlbumAdapter albumAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
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

        viewModel.getAllAlbums().observe(getViewLifecycleOwner(),
                newAlbums -> albumAdapter.updateAlbums(newAlbums));
        viewModel.loadAllAlbums(AlbumsRepository.SortBy.NAME, SortOrder.ASC);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        setDisplayModeAsGrid(); // Default display mode is grid

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.album_tab_options_menu, menu);

        SearchManager searchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.album_tab_search).getActionView();

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
        if (item.getItemId() == R.id.album_tab_show_as_grid)
            setDisplayModeAsGrid();
        else if (item.getItemId() == R.id.album_tab_show_as_list)
            setDisplayModeAsList();
        else if (item.getItemId() == R.id.album_tab_sort_by_name_asc)
            viewModel.loadAllAlbums(AlbumsRepository.SortBy.NAME, SortOrder.ASC);
        else if (item.getItemId() == R.id.album_tab_sort_by_name_desc)
            viewModel.loadAllAlbums(AlbumsRepository.SortBy.NAME, SortOrder.DESC);
        else if (item.getItemId() == R.id.album_tab_sort_by_number_of_songs_asc)
            viewModel.loadAllAlbums(AlbumsRepository.SortBy.NUMBER_OF_SONGS, SortOrder.ASC);
        else if (item.getItemId() == R.id.album_tab_sort_by_number_of_songs_desc)
            viewModel.loadAllAlbums(AlbumsRepository.SortBy.NUMBER_OF_SONGS, SortOrder.DESC);
        else if (item.getItemId() == R.id.album_tab_sort_by_first_year_asc)
            viewModel.loadAllAlbums(AlbumsRepository.SortBy.FIRST_YEAR, SortOrder.ASC);
        else if (item.getItemId() == R.id.album_tab_sort_by_first_year_desc)
            viewModel.loadAllAlbums(AlbumsRepository.SortBy.FIRST_YEAR, SortOrder.DESC);

        return true;
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
}