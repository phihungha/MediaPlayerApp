package com.example.mediaplayerapp.ui.playlist;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.databinding.FragmentPlaylistBinding;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.playlist.media_queue.MediaQueueFragment;
import com.example.mediaplayerapp.ui.playlist.playlist_details.PlaylistDetailsFragment;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PlaylistFragment extends Fragment implements View.OnClickListener {
    private FragmentPlaylistBinding binding;
    private final PlaylistDetailsFragment detailsFragment = new PlaylistDetailsFragment();
    private final MediaQueueFragment mediaQueueFragment = new MediaQueueFragment();
    private PlaylistAdapter adapter;
    private PlaylistViewModel playlistViewModel;

    private boolean isASC = false;

    BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PlaylistAdapter(new PlaylistAdapter.PlaylistDiff());
        binding.rcvPlaylists.setAdapter(adapter);
        playlistViewModel.getAllPlaylists().observe(
                getViewLifecycleOwner(),
                playlists -> {
                    // Update the cached copy of the playlist in the adapter.
                    adapter.submitList(playlists);
                }
        );
        setListenerForAdapter();
    }

    private void setListenerForAdapter() {
        binding.layoutItemAddPlaylist.setOnClickListener(this);
        binding.layoutItemWatchLater.setOnClickListener(this);
        binding.layoutItemMyFavourite.setOnClickListener(this);

        adapter.setContext(requireContext());
        adapter.setApplication(requireActivity().getApplication());
        //set click item listener for recyclerview
        adapter.setListener((v, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PlaylistConstants.KEY_TRANSFER_PLAYLIST, adapter.getPlaylistItemAt(position));
            detailsFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //click bottom sheet play item recyclerview
        adapter.setBSPlayListener((view, position) -> {
            Playlist playlist = adapter.getPlaylistItemAt(position);
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
            if (playlist.isVideo()) {
                VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            } else {
                MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
        });

        //click bottom sheet rename item recyclerview
        adapter.setBSRenameListener((view, position) -> {
            Playlist playlist = adapter.getPlaylistItemAt(position);
            PlaylistRenameDialog dialog = PlaylistRenameDialog.newInstance(playlist);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_RENAME_DIALOG);
        });
        //click bottom sheet delete item recyclerview
        adapter.setBSDeleteListener(((view, position) -> {
            Playlist playlist = adapter.getPlaylistItemAt(position);
            PlaylistDeleteDialog dialog = PlaylistDeleteDialog.newInstance(playlist);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DELETE_DIALOG);
        }));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutItem_addPlaylist:
                openBottomSheetDialogAddPlaylist();
                break;

            case R.id.layoutItem_myFavourite:
                openFavourite();
                break;

            case R.id.layoutItem_watchLater:
                openWatchLaterList();
                break;
        }
    }

    private void openFavourite() {
        Bundle bundle=new Bundle();
        bundle.putString(PlaylistConstants.TRANS,PlaylistConstants.TRANS_FAVOURITE);
        mediaQueueFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, mediaQueueFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openWatchLaterList() {
        Bundle bundle=new Bundle();
        bundle.putString(PlaylistConstants.TRANS,PlaylistConstants.TRANS_QUEUE);
        mediaQueueFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, mediaQueueFragment)
                .addToBackStack(null)
                .commit();
    }

    private void makeToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Open BottomSheet Dialog Add playlist
     */
    private void openBottomSheetDialogAddPlaylist() {
        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetTheme);
        View bsAddView = LayoutInflater.from(getContext()).inflate(
                R.layout.bottom_sheet_playlist_create,
                requireActivity().findViewById(R.id.bs_playlist_create)
        );
        //set click event here
        EditText edtName = bsAddView.findViewById(R.id.edt_playlistNameCreate);
        Button btnCreate = bsAddView.findViewById(R.id.btn_createPlaylist);
        RadioButton radioAudio = bsAddView.findViewById(R.id.radio_audio);
        RadioButton radioVideo = bsAddView.findViewById(R.id.radio_video);
        btnCreate.setOnClickListener(view -> {

            if (edtName.getText().toString().trim().isEmpty()) {
                makeToast("Name is empty!");
            } else if (!radioAudio.isChecked() && !radioVideo.isChecked()) {
                makeToast("Please check type for playlist!");
            } else {
                int idResource;
                if (!radioAudio.isChecked()) {
                    idResource = R.drawable.ic_play_video_24dp;
                } else {
                    idResource = R.drawable.ic_music_video_24;
                }
                Playlist playlist = new Playlist(idResource,
                        edtName.getText().toString().trim(),
                        radioVideo.isChecked(),
                        0
                );
                playlistViewModel.insert(playlist);

                bottomSheetDialog.dismiss();
                makeToast("Create Playlist Success!");
            }
        });

        bottomSheetDialog.setContentView(bsAddView);
        bottomSheetDialog.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_options_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setIconified(true);
        searchView.setQueryHint(PlaylistConstants.STRING_HINT_SEARCH);

        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Searching(s);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort)
            SortByName();
        return super.onOptionsItemSelected(item);
    }

    private void SortByName() {
        if (isASC) {
            playlistViewModel.sortPlaylistByNameDESC().observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists));

        } else {
            playlistViewModel.sortPlaylistByNameASC().observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists));
            isASC = !isASC;
        }
    }

    private void Searching(String s) {
        if (s.equals("")) {
            playlistViewModel.getAllPlaylists().observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        } else {
            playlistViewModel.getAllPlaylistSearching(s).observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        }
    }
}
