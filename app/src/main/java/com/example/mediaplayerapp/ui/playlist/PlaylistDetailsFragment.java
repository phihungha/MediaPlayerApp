package com.example.mediaplayerapp.ui.playlist;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.example.mediaplayerapp.utils.MessageUtils;
import com.example.mediaplayerapp.utils.item_touch_helper.SimpleItemTouchHelperCallback;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = PlaylistDetailsFragment.class.getSimpleName();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private boolean playlistLoaded = false;
    private int currentPlaylistId;
    private Playlist playlist;

    private ActivityResultLauncher<String[]> mediaPickerLauncher;

    private PlaylistItemViewModel playlistItemViewModel;
    private PlaylistItemAdapter adapter;

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private FragmentPlaylistDetailsBinding binding;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        playlistItemViewModel = new ViewModelProvider(this).get(PlaylistItemViewModel.class);
        PlaylistViewModel playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        // Activity result launcher needs to be register
        // again every time this fragment's views are re-created
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                this::addPickedUrisIntoPlaylist
        );

        binding.playlistDetailsAddBtn.setOnClickListener(view -> addPlaylistItems());
        binding.playlistDetailsPlayAllBtn.setOnClickListener(view -> playAll());
        binding.playlistDetailsShuffleAllBtn.setOnClickListener(view -> playShuffleAll());

        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());

        currentPlaylistId = PlaylistDetailsFragmentArgs.fromBundle(requireArguments()).getPlaylistId();

        playlistViewModel.getPlaylist(currentPlaylistId).observe(
                getViewLifecycleOwner(),
                newPlaylist -> {
                    playlist = newPlaylist;
                    updatePlaylistInfoHeader();
                    if (!playlistLoaded) {
                        setupPlaylistItemRecyclerView();
                        playlistLoaded = true;
                    }
                }
        );

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * This method depends on fully loaded playlist information.
     * This method should only be called when
     * the playlist variable is not null.
     */
    private void setupPlaylistItemRecyclerView() {
        if (playlist == null)
            Log.e(LOG_TAG, "Playlist has not been loaded!");

        adapter = new PlaylistItemAdapter(
                this,
                playlistItemViewModel,
                disposables,
                playlist.isVideo()
        );
        binding.playlistDetailsItemList.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.playlistDetailsItemList);

        playlistItemViewModel.getAllItemsOfPlaylist(currentPlaylistId)
                .observe(getViewLifecycleOwner(),
                        playlistItems -> adapter.submitList(playlistItems)
                );

        setDisplayModeAsList();
    }

    public void updatePlaylistInfoHeader() {
        binding.playlistDetailsName.setText(playlist.getName());
        setItemCount();

        playlistItemViewModel.getFirstItemOfPlaylist(currentPlaylistId)
                .observe(getViewLifecycleOwner(),
                         playlistItems -> {
                             Uri thumbnailUri = Uri.EMPTY;
                             if (!playlistItems.isEmpty())
                                 thumbnailUri = playlistItems.get(0).getAndroidMediaUri();

                            Drawable thumbnail = MediaMetadataUtils.getThumbnail(
                                    requireContext(),
                                    thumbnailUri,
                                    R.drawable.ic_playlist_24dp);
                            binding.playlistDetailsItemThumbnail.setImageDrawable(thumbnail);
                        });
    }

    private void setItemCount() {
        String text = playlist.getItemCount() + " ";
        if (playlist.isVideo())
            text += requireContext().getString(R.string.video_count_postfix);
        else
            text += requireContext().getString(R.string.song_count_postfix);
        binding.playlistDetailsItemCount.setText(text);
    }

    private void playAll() {
        if (playlist.getItemCount() == 0) {
            Toast.makeText(requireContext(),
                    R.string.no_playlist_item_to_play,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(currentPlaylistId, 0);
        if (playlist.isVideo()) {
            VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        } else {
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        }
    }

    private void playShuffleAll() {
        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(currentPlaylistId, 0);
        if (playlist.isVideo()) {
            VideoPlayerActivity.launchWithUriAndShuffleAll(requireActivity(), playbackUri);
        } else {
            MusicPlayerActivity.launchWithUriAndShuffleAll(requireActivity(), playbackUri);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.playlist_details_add_btn) {
            addPlaylistItems();
        } else if (id == R.id.playlist_details_play_all_btn) {
            playAll();
        } else if (id == R.id.playlist_details_shuffle_all_btn) {
            playShuffleAll();
        }
    }

    private void addPlaylistItems() {
        if (playlist.isVideo())
            mediaPickerLauncher.launch(new String[]{"video/*"});
        else
            mediaPickerLauncher.launch(new String[]{"audio/*"});
    }

    private void addPickedUrisIntoPlaylist(List<Uri> uris) {
        if (uris.isEmpty())
            return;

        List<PlaylistItem> newItems;
        try {
            newItems = uris.stream()
                           .map(this::getPlaylistItemFromMediaUri)
                           .collect(Collectors.toList());
        } catch (RuntimeException err) {
            Toast.makeText(requireContext(),
                            "Add items from downloads folder not yet supported",
                            Toast.LENGTH_SHORT).show();
            return;
        }

        Disposable disposable = playlistItemViewModel
                 .addPlaylistItems(newItems)
                 .subscribe(
                         () -> {},
                         e -> {
                             String message = e.getMessage();
                             if (message != null && !message.contains("UNIQUE"))
                                 MessageUtils.displayError(
                                         requireContext(),
                                         LOG_TAG,
                                         e.getMessage());
                         }
                 );
        disposables.add(disposable);
    }

    private PlaylistItem getPlaylistItemFromMediaUri(Uri uri) {
        String mediaStoreUri = MediaStore.getMediaUri(requireContext(), uri).toString();
        return new PlaylistItem(currentPlaylistId, mediaStoreUri);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_details_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_show_as_list_playlist_detail)
            setDisplayModeAsList();
        else if (item.getItemId() == R.id.action_show_as_grid_playlist_detail)
            setDisplayModeAsGrid();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposables.dispose();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        binding.playlistDetailsItemList.setHasFixedSize(true);
        binding.playlistDetailsItemList.setLayoutManager(gridLayoutManager);
        adapter.setDisplayMode(DisplayMode.GRID);
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        binding.playlistDetailsItemList.setHasFixedSize(true);
        binding.playlistDetailsItemList.setLayoutManager(linearLayoutManager);
        adapter.setDisplayMode(DisplayMode.LIST);
    }
}
