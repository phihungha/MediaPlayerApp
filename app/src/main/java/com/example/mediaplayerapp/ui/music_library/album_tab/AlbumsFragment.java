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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.AlbumsRepository;
import com.example.mediaplayerapp.databinding.FragmentAlbumsBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.utils.SortOrder;

@SuppressLint("NotifyDataSetChanged")
public class AlbumsFragment extends Fragment {

    private static final String CURRENT_DISPLAY_MODE_KEY = "current_display_mode";
    private static final String CURRENT_SORT_BY_KEY = "current_sort_by";
    private static final String CURRENT_SORT_ORDER_KEY = "current_sort_order";

    private static final int GRID_MODE_COLUMN_NUM = 2;

    private AlbumAdapter albumAdapter;

    private DisplayMode currentDisplayMode;
    private AlbumsRepository.SortBy currentSortBy;
    private SortOrder currentSortOrder;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private AlbumsViewModel viewModel;

    private FragmentAlbumsBinding binding;

    public AlbumsFragment() {
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
        binding = FragmentAlbumsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AlbumsViewModel.class);

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());

        albumAdapter = new AlbumAdapter(requireContext());
        binding.albumList.setAdapter(albumAdapter);

        binding.albumsSwipeRefreshContainer.setOnRefreshListener(
                () -> viewModel.loadAllAlbums(currentSortBy, currentSortOrder)
        );
        binding.albumsSwipeRefreshContainer.setColorSchemeResources(R.color.cyan);

        viewModel.getAllAlbums().observe(getViewLifecycleOwner(),
                newAlbums ->  {
                    albumAdapter.updateAlbums(newAlbums);
                    binding.albumsSwipeRefreshContainer.setRefreshing(false);
                });

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        } else {
            setSortMode(AlbumsRepository.SortBy.NAME, SortOrder.ASC);
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
        currentSortBy = (AlbumsRepository.SortBy) savedInstanceState.getSerializable(CURRENT_SORT_BY_KEY);
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
            setSortMode(AlbumsRepository.SortBy.NAME, SortOrder.ASC);
        else if (item.getItemId() == R.id.album_tab_sort_by_name_desc)
            setSortMode(AlbumsRepository.SortBy.NAME, SortOrder.DESC);
        else if (item.getItemId() == R.id.album_tab_sort_by_number_of_songs_asc)
            setSortMode(AlbumsRepository.SortBy.NUMBER_OF_SONGS, SortOrder.ASC);
        else if (item.getItemId() == R.id.album_tab_sort_by_number_of_songs_desc)
            setSortMode(AlbumsRepository.SortBy.NUMBER_OF_SONGS, SortOrder.DESC);
        else if (item.getItemId() == R.id.album_tab_sort_by_first_year_asc)
            setSortMode(AlbumsRepository.SortBy.FIRST_YEAR, SortOrder.ASC);
        else if (item.getItemId() == R.id.album_tab_sort_by_first_year_desc)
            setSortMode(AlbumsRepository.SortBy.FIRST_YEAR, SortOrder.DESC);

        return true;
    }

    /**
     * Change current sort mode.
     * @param sortBy Sort by what
     * @param sortOrder Sort order
     */
    private void setSortMode(AlbumsRepository.SortBy sortBy, SortOrder sortOrder) {
        viewModel.loadAllAlbums(sortBy, sortOrder);
        currentSortBy = sortBy;
        currentSortOrder = sortOrder;
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        if (currentDisplayMode == DisplayMode.GRID)
            return;
        binding.albumList.setLayoutManager(gridLayoutManager);
        albumAdapter.setDisplayMode(DisplayMode.GRID);
        currentDisplayMode = DisplayMode.GRID;
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        if (currentDisplayMode == DisplayMode.LIST)
            return;
        binding.albumList.setLayoutManager(linearLayoutManager);
        albumAdapter.setDisplayMode(DisplayMode.LIST);
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