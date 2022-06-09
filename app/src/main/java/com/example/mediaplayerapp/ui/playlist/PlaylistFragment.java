package com.example.mediaplayerapp.ui.playlist;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistRepository;
import com.example.mediaplayerapp.databinding.BottomSheetPlaylistAddBinding;
import com.example.mediaplayerapp.databinding.FragmentPlaylistsBinding;
import com.example.mediaplayerapp.utils.SortOrder;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PlaylistFragment extends Fragment {

    private PlaylistViewModel viewModel;

    private PlaylistAdapter adapter;

    private BottomSheetDialog bottomSheetDialog;
    private FragmentPlaylistsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        setupAddDialog();
        binding.playlistAddBtn.setOnClickListener(view -> bottomSheetDialog.show());

        adapter = new PlaylistAdapter(
                this, new ViewModelProvider(this).get(PlaylistItemViewModel.class)
        );
        binding.playlistList.setAdapter(adapter);

        loadAllPlaylists(PlaylistRepository.SortBy.NAME, SortOrder.ASC);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupAddDialog() {
        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetTheme);
        BottomSheetPlaylistAddBinding bottomSheetBinding
                = BottomSheetPlaylistAddBinding.inflate(
                        getLayoutInflater(),
                        requireActivity().findViewById(R.id.bs_playlist_create),
                    false);

        bottomSheetBinding.playlistAddBottomSheetCreateBtn.setOnClickListener(view -> {
            String name = bottomSheetBinding.playlistAddBottomSheetName
                            .getText()
                            .toString()
                            .trim();

            if (name.isEmpty())
                Toast.makeText(requireActivity(), R.string.name_is_empty, Toast.LENGTH_SHORT).show();
            else {
                Playlist playlist = new Playlist(
                         name,
                         bottomSheetBinding.playlistAddBottomSheetVideo.isChecked(),
                        0);
                viewModel.insertPlaylist(playlist).subscribe();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_options_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search_playlist_main);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setIconified(true);
        searchView.setQueryHint(PlaylistConstants.STRING_HINT_SEARCH);

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchPlaylistsByNameMatching(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchPlaylistsByNameMatching(s);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_sort_playlist_by_title_asc)
           loadAllPlaylists(PlaylistRepository.SortBy.NAME, SortOrder.ASC);
        else if(item.getItemId() == R.id.action_sort_playlist_by_title_desc)
            loadAllPlaylists(PlaylistRepository.SortBy.NAME, SortOrder.DESC);
        else if(item.getItemId() == R.id.action_sort_playlist_by_number_item_asc)
            loadAllPlaylists(PlaylistRepository.SortBy.ITEM_COUNT, SortOrder.ASC);
        else if(item.getItemId() == R.id.action_sort_playlist_by_number_item_desc)
            loadAllPlaylists(PlaylistRepository.SortBy.ITEM_COUNT, SortOrder.DESC);
        return true;
    }

    private void loadAllPlaylists(PlaylistRepository.SortBy sortBy, SortOrder sortOrder) {
        viewModel.getAllPlaylists(sortBy, sortOrder).observe(
                getViewLifecycleOwner(),
                playlists -> adapter.submitList(playlists)
        );
    }

    private void searchPlaylistsByNameMatching(String searchTerm) {
        if (searchTerm.equals(""))
            loadAllPlaylists(PlaylistRepository.SortBy.NAME, SortOrder.ASC);
        else {
            viewModel.getPlaylistsByNameMatching(searchTerm).observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        }
    }
}
