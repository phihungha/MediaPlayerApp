package com.example.mediaplayerapp.ui.playlist;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.databinding.FragmentPlaylistBinding;
import com.example.mediaplayerapp.ui.playlist.media_queue.MediaQueueFragment;
import com.example.mediaplayerapp.ui.playlist.playlist_details.PlaylistDetailsFragment;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMedia;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMediaViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements View.OnClickListener {
    private FragmentPlaylistBinding binding;
    private PlaylistDetailsFragment detailsFragment = new PlaylistDetailsFragment();
    private MediaQueueFragment mediaQueueFragment=new MediaQueueFragment();
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
                new Observer<List<Playlist>>() {
                    @Override
                    public void onChanged(List<Playlist> playlists) {
                        // Update the cached copy of the playlist in the adapter.
                        adapter.submitList(playlists);
                    }
                }
        );
        setListenerForAdapter();
    }

    private void setListenerForAdapter() {
        binding.layoutItemAddPlaylist.setOnClickListener(this);

        adapter.setApplication(getActivity().getApplication());
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
            Playlist playlist=adapter.getPlaylistItemAt(position);

            PlaylistMediaViewModel playlistMediaViewModel = new ViewModelProvider(this)
                    .get(PlaylistMediaViewModel.class);
            playlistMediaViewModel.getAllPlaylistMediasWithID(playlist.getId()).observe(
                    getViewLifecycleOwner(),
                    new Observer<List<PlaylistMedia>>() {
                        @Override
                        public void onChanged(List<PlaylistMedia> media) {
                            //list uri of Media need to play
                            List<Uri> listUriMedia = new ArrayList<>();
                            media.forEach(item ->{
                                listUriMedia.add(Uri.parse(item.getMediaUri()));
                            });
                            /**
                             *
                             *
                             *        Play LINEAR playlists with listUriMedia HERE
                             *
                             *
                             *
                             * */
                            makeToast("Play at pos " + position);
                        }
                    }
            );
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutItem_addPlaylist:
                openBottomSheetDialogAddPlaylist();
                break;
        }
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
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);
        View bsAddView = LayoutInflater.from(getContext()).inflate(
                R.layout.playlist_create_bs_layout,
                getActivity().findViewById(R.id.bs_playlist_create)
        );
        //set click event here
        EditText edtName = bsAddView.findViewById(R.id.edt_playlistNameCreate);
        Button btnCreate = bsAddView.findViewById(R.id.btn_createPlaylist);
        RadioButton radioAudio = bsAddView.findViewById(R.id.radio_audio);
        RadioButton radioVideo = bsAddView.findViewById(R.id.radio_video);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtName.getText().toString().trim().isEmpty()) {
                    makeToast("Name is empty!");
                } else if (!radioAudio.isChecked() && !radioVideo.isChecked()) {
                    makeToast("Please check type for playlist!");
                } else {
                    Playlist playlist = new Playlist(R.drawable.img_for_test,
                            edtName.getText().toString().trim(), radioVideo.isChecked());
                    playlistViewModel.insert(playlist);

                    bottomSheetDialog.dismiss();
                    makeToast("Create Playlist Success!");
                }
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
        inflater.inflate(R.menu.option_menu_playlist, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setIconified(true);
        searchView.setQueryHint(PlaylistConstants.STRING_HINT_SEARCH);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

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
        switch (item.getItemId()) {
            case R.id.action_sort:
                SortByName();
                break;

            case R.id.action_queue:
                GoToQueue();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void GoToQueue() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, mediaQueueFragment)
                .addToBackStack(null)
                .commit();
    }

    private void SortByName() {
        if (isASC) {
            playlistViewModel.sortPlaylistByNameDESC().observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        } else {
            playlistViewModel.sortPlaylistByNameASC().observe(
                    getViewLifecycleOwner(),
                    playlists -> adapter.submitList(playlists)
            );
        }
        isASC = !isASC;
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
