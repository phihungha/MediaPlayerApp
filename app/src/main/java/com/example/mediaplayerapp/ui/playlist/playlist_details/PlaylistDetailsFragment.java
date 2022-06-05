package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueUtil;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.databinding.FragmentPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.GridSpacingItemDecoration;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;

import java.util.List;

public class PlaylistDetailsFragment extends Fragment implements View.OnClickListener, OnStartDragListener, OnPlaylistItemListChangedListener {
    private Playlist playlist;
    private FragmentPlaylistDetailsBinding binding;
    private MediaItemAdapter adapter;
    private PlaylistItemViewModel playlistItemViewModel;
    private PlaylistViewModel playlistViewModel;
    private ActivityResultLauncher<String[]> mediaPickerLauncher;


    private static final int GRID_MODE_COLUMN_NUM = 2;
    private static final int GRID_MODE_SPACING = 30;
    private DisplayMode currentDisplayMode;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    private ItemTouchHelper mItemTouchHelper;

    public PlaylistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Activity result launcher needs to be register
        // again every time this fragment is re-created
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                this::addPickedMediaItemsIntoPlaylist
        );
        binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false);
        playlistItemViewModel = new ViewModelProvider(this).get(PlaylistItemViewModel.class);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            playlist = (Playlist) bundle.getSerializable(PlaylistConstants.KEY_TRANSFER_PLAYLIST);
        }

        adapter = new MediaItemAdapter(new MediaItemAdapter.PlaylistMediaDiff());
        adapter.setContext(requireContext());
        adapter.setApplication(requireActivity().getApplication());
        adapter.setPlaylist(playlist);
        playlistItemViewModel.getAllPlaylistMediasWithID(playlist.getId()).observe(
                getViewLifecycleOwner(),
                media -> adapter.submitList(media)
        );

        setUpRecyclerView();
        setListener();

        currentDisplayMode = DisplayMode.LIST;
        setDisplayModeAsList();
        refresh();
    }

    private void setUpRecyclerView() {
        binding.rcvPlaylistsDetails.setHasFixedSize(true);
        binding.rcvPlaylistsDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setDragStartListener(this);
        adapter.setListChangedListener(this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rcvPlaylistsDetails);

        binding.rcvPlaylistsDetails.setAdapter(adapter);
    }

    public void refresh() {
        int count = playlistItemViewModel.getCountPlaylistWithID(playlist.getId());
        playlist.setCount(count);

        binding.tvPlaylistDetailsName.setText(playlist.getName());
        binding.tvPlaylistDetailsNumbers.setText(getStringCountText(playlist));

        PlaylistItem playlistItem = playlistItemViewModel.findByItemId(playlist.getId());
        if (playlistItem == null) {
            binding.imgThumbnailPlaylistDetails.setImageResource(playlist.getIdResource());
            return;
        }
        if (playlist.isVideo()) {
            Glide.with(requireContext())
                    .load(playlistItem.getMediaUri())
                    .skipMemoryCache(false)
                    .error(playlist.getIdResource())
                    .centerCrop()
                    .into(binding.imgThumbnailPlaylistDetails);
        } else {
            Bitmap thumb = MediaUtils.loadThumbnail(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            if (thumb != null) {
                binding.imgThumbnailPlaylistDetails.setImageBitmap(thumb);
            } else {
                binding.imgThumbnailPlaylistDetails.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(),
                                playlist.getIdResource()));
            }
        }
        playlistViewModel.update(playlist);
    }

    private String getStringCountText(Playlist playlist) {
        int count = playlist.getCount();
        String textNumber = "Playlist " + count + " ";

        if (playlist.isVideo()) {
            if (count <= 1) {
                textNumber += "video";
            } else
                textNumber += "videos";
        } else {
            if (count <= 1) {
                textNumber += "song";
            } else
                textNumber += "songs";
        }
        return textNumber;
    }

    private void setListener() {
        binding.btnAddMore.setOnClickListener(this);
        binding.layoutPlayAll.setOnClickListener(this);
        binding.layoutShuffleAll.setOnClickListener(this);

        //item click
        adapter.setItemClickListener((v, position) -> {
            PlaylistItem playlistItem = adapter.getPlaylistMediaItemAt(position);
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlistItem.getId(), position);
            if (playlist.isVideo()) {
                VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            } else {
                MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
        });

        // click play bottom sheet
        adapter.setBsPlayListener((view, position) -> {
            PlaylistItem playlistItem = adapter.getPlaylistMediaItemAt(position);
            Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlistItem.getId(), position);
            if (playlist.isVideo()) {
                VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            } else {
                MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
            }
        });
        // click delete bottom sheet
        adapter.setBsDeleteListener((view, position) -> {
            PlaylistItem media = adapter.getPlaylistMediaItemAt(position);
            PlaylistDetailsDeleteDialog dialog = PlaylistDetailsDeleteDialog.newInstance(media);
            dialog.setPlaylistDetailsFragment(this);
            dialog.setPlaylist(playlist);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DETAIL_DELETE_DIALOG);
        });

        //click add to queue bottom sheet
        adapter.setBsAddQueueListener((view, position) -> {
            PlaylistItem media = adapter.getPlaylistMediaItemAt(position);
            if (playlist.isVideo()) {
                MediaQueueUtil.insertVideoToWatchLater(
                        requireActivity().getApplication(),
                        media.getMediaUri()
                );
            } else {
                MediaQueueUtil.insertSongToWatchLater(
                        requireActivity().getApplication(),
                        media.getMediaUri()
                );
            }

            Toast.makeText(getContext(), "Add to queue completed", Toast.LENGTH_SHORT).show();
        });

        //click add to favourite bottom sheet
        adapter.setBsAddFavouriteListener((view, position) -> {
            PlaylistItem media = adapter.getPlaylistMediaItemAt(position);

            if (playlist.isVideo()) {
                MediaQueueUtil.insertVideoToFavourite(
                        requireActivity().getApplication(),
                        media.getMediaUri()
                );
            } else {
                MediaQueueUtil.insertSongToFavourite(
                        requireActivity().getApplication(),
                        media.getMediaUri()
                );
            }

            Toast.makeText(getContext(), "Add to favourite completed", Toast.LENGTH_SHORT).show();
        });

        //click properties bottom sheet
        adapter.setBsPropertiesListener((view, position) -> {
            PlaylistItem media = adapter.getPlaylistMediaItemAt(position);

            MediaInfo mediaInfo = MediaUtils.getInfoWithUri(requireContext(),
                    Uri.parse(media.getMediaUri()));

            PlaylistDetailsPropertiesDialog dialog = PlaylistDetailsPropertiesDialog.newInstance(mediaInfo);
            dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_BS_DETAIL_PROPERTY_DIALOG);
        });
    }

    private void PlayAll() {
        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
        if (playlist.isVideo()) {
            VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        } else {
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        }
    }

    private void PlayShuffleAll() {
        Uri playbackUri = GetPlaybackUriUtils.forPlaylist(playlist.getId(), 0);
        if (playlist.isVideo()) {
            VideoPlayerActivity.launchWithUriAndShuffleAll(requireActivity(), playbackUri);
        } else {
            MusicPlayerActivity.launchWithUriAndShuffleAll(requireActivity(), playbackUri);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_addMore) {
            AddMoreMedia();
        } else if (id == R.id.layout_playAll) {
            PlayAll();
        } else if (id == R.id.layout_shuffleAll) {
            PlayShuffleAll();
        }
    }

    private void AddMoreMedia() {
        if (playlist.isVideo()) {
            mediaPickerLauncher.launch(new String[]{"video/*"});
        } else {
            mediaPickerLauncher.launch(new String[]{"audio/*"});
        }
    }

    private void addPickedMediaItemsIntoPlaylist(List<Uri> uris) {
        if (uris.isEmpty())
            return;

        uris.forEach(uri -> {
            long order = MediaUtils.generateOrderSort();
            PlaylistItem media = new PlaylistItem(
                    playlist.getId(),
                    uri.toString(),
                    order);
            playlistItemViewModel.insert(media);
        });

        refresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_detail_option_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.action_search_playlist_detail);
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

                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort_by_title_asc_playlist_detail)
            SortByNameASC();
        else if (item.getItemId() == R.id.action_sort_by_title_desc_playlist_detail)
            SortByNameDESC();
        else if (item.getItemId() == R.id.action_sort_by_duration_asc_playlist_detail)
            SortByDurationASC();
        else if (item.getItemId() == R.id.action_sort_by_duration_desc_playlist_detail)
            SortByDurationDESC();
        else if (item.getItemId() == R.id.action_show_as_list_playlist_detail)
            ShowAsList();
        else if (item.getItemId() == R.id.action_show_as_grid_playlist_detail)
            ShowAsGrid();

        return super.onOptionsItemSelected(item);
    }

    private void SortByNameASC() {
        List<PlaylistItem> current = playlistItemViewModel.getCurrentList();
        current.sort((playlistItem, t1) -> {
            String name1 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            String name2 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(t1.getMediaUri()));
            return name1.compareTo(name2);
        });

        for (int i = 0; i < current.size(); i++) {
            PlaylistItem item = current.get(i);
            item.setOrderSort(i);
        }
        playlistItemViewModel.updateByList(current);


    }

    private void SortByNameDESC() {
        List<PlaylistItem> current = playlistItemViewModel.getCurrentList();
        current.sort((playlistItem, t1) -> {
            String name1 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            String name2 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(t1.getMediaUri()));
            return name2.compareTo(name1);
        });

        for (int i = 0; i < current.size(); i++) {
            PlaylistItem item = current.get(i);
            item.setOrderSort(i);
        }
        playlistItemViewModel.updateByList(current);
    }

    private void SortByDurationASC() {
        List<PlaylistItem> current = playlistItemViewModel.getCurrentList();
        current.sort((playlistItem, t1) -> {
            Long dur1 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            Long dur2 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(t1.getMediaUri()));
            return dur1.compareTo(dur2);
        });

        for (int i = 0; i < current.size(); i++) {
            PlaylistItem item = current.get(i);
            item.setOrderSort(i);
        }
        playlistItemViewModel.updateByList(current);
    }

    private void SortByDurationDESC() {
        List<PlaylistItem> current = playlistItemViewModel.getCurrentList();
        current.sort((playlistItem, t1) -> {
            Long dur1 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            Long dur2 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(t1.getMediaUri()));
            return dur2.compareTo(dur1);
        });

        for (int i = 0; i < current.size(); i++) {
            PlaylistItem item = current.get(i);
            item.setOrderSort(i);
        }
        playlistItemViewModel.updateByList(current);
    }

    private void ShowAsList() {
        setDisplayModeAsList();
    }

    private void ShowAsGrid() {
        setDisplayModeAsGrid();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onNoteListChanged(List<PlaylistItem> list) {
        Log.d("TAG", "LIST_CHANGE");
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        if (currentDisplayMode == DisplayMode.GRID)
            return;

        binding.rcvPlaylistsDetails.setLayoutManager(gridLayoutManager);
        binding.rcvPlaylistsDetails.addItemDecoration(
                new GridSpacingItemDecoration(GRID_MODE_COLUMN_NUM,
                        GRID_MODE_SPACING,
                        true));

        adapter.setDisplayMode(DisplayMode.GRID);
        currentDisplayMode = DisplayMode.GRID;
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        if (currentDisplayMode == DisplayMode.LIST)
            return;

        binding.rcvPlaylistsDetails.setLayoutManager(linearLayoutManager);
        binding.rcvPlaylistsDetails.removeItemDecorationAt(0);

        adapter.setDisplayMode(DisplayMode.LIST);
        currentDisplayMode = DisplayMode.LIST;
    }
}
