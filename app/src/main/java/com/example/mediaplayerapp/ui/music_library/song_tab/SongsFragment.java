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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentSongsBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;

@SuppressLint("NotifyDataSetChanged")
public class SongsFragment extends Fragment {

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;

    private SongAdapter songAdapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private FragmentSongsBinding binding;

    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSongsBinding.inflate(getLayoutInflater(), container, false);
        SongsViewModel viewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        setHasOptionsMenu(true);

        songAdapter = new SongAdapter(requireContext(), orderIndex -> {
            Uri playbackUri = GetPlaybackUriUtils.forLibrary(orderIndex);
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        });
        binding.songList.setAdapter(songAdapter);

        viewModel.getAllSongs().observe(getViewLifecycleOwner(), newSongs -> songAdapter.updateSongs(newSongs));

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
        if (item.getItemId() == R.id.show_as_grid)
            setDisplayModeAsGrid();
        else if (item.getItemId() == R.id.show_as_list)
            setDisplayModeAsList();

        return true;
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        binding.songList.setLayoutManager(gridLayoutManager);
        binding.songList.addItemDecoration(
                new GridSpacingItemDecoration(GRID_MODE_COLUMN_NUM,
                        GRID_MODE_SPACING,
                        true));
        songAdapter.setDisplayMode(DisplayMode.GRID);
        songAdapter.notifyDataSetChanged();

    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        binding.songList.setLayoutManager(linearLayoutManager);
        binding.songList.removeItemDecorationAt(0);
        songAdapter.setDisplayMode(DisplayMode.LIST);
        songAdapter.notifyDataSetChanged();
    }
}