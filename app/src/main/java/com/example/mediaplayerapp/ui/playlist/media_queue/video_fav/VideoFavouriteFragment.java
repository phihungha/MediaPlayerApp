package com.example.mediaplayerapp.ui.playlist.media_queue.video_fav;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.databinding.FragmentVideoFavouriteBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.ui.playlist.media_queue.MediaQueueAdapter;
import com.example.mediaplayerapp.ui.playlist.media_queue.MediaQueueDeleteDialog;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.MediaUtils;
import com.example.mediaplayerapp.ui.playlist.OnPlaylistItemListChangedListener;
import com.example.mediaplayerapp.utils.item_touch.OnStartDragListener;
import com.example.mediaplayerapp.utils.item_touch.SimpleItemTouchHelperCallback;

import java.util.List;

public class VideoFavouriteFragment extends Fragment implements OnStartDragListener, OnPlaylistItemListChangedListener {
    private FragmentVideoFavouriteBinding binding;
    private MediaQueueViewModel viewModel;
    private MediaQueueAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    public VideoFavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoFavouriteBinding.inflate(inflater, container, false);
        viewModel=new ViewModelProvider(this).get(MediaQueueViewModel.class);
        gridLayoutManager = new GridLayoutManager(getContext(), GRID_MODE_COLUMN_NUM);
        linearLayoutManager = new LinearLayoutManager(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter=new MediaQueueAdapter(new MediaQueueAdapter.MediaQueueDiff());
        adapter.setContext(requireContext());
        adapter.setType(PlaylistConstants.TYPE_VIDEO_FAVOURITE);
        viewModel.getAllVideoFavourite().observe(
                getViewLifecycleOwner(),
                mediaQueues -> adapter.submitList(mediaQueues)
        );

        setDisplayModeAsList();

        setUpRecyclerView();
        setAdapterListener();
    }
    private void setUpRecyclerView(){
        adapter.setDragStartListener(this);
        adapter.setViewModel(viewModel);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rcvVideoFavourite);

        binding.rcvVideoFavourite.setAdapter(adapter);
    }
    private void setAdapterListener() {
        // item click
        adapter.setItemClickListener(this::ClickItem);
        //item delete
        adapter.setDeleteItemListener(this::DeleteItemQueue);
    }

    private void ClickItem(View view, int position) {
        MediaQueue mediaQueue=adapter.getItemAt(position);
        Uri playbackUri = Uri.parse(mediaQueue.getMediaUri());
        VideoPlayerActivity.launchWithUri(requireActivity(), playbackUri);
    }

    private void DeleteItemQueue(View view, int position) {
        MediaQueue mediaQueue = adapter.getItemAt(position);
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(mediaQueue);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_QUEUE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.playlist_detail_option_menu, menu);
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
        List<MediaQueue> current = viewModel.getCurrentListVideoFavourite();
        current.sort((playlistItem, t1) -> {
            String name1 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            String name2 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(t1.getMediaUri()));
            return name1.compareTo(name2);
        });

        for (int i = 0; i < current.size(); i++) {
            MediaQueue item = current.get(i);
            item.setOrderSort(i);
        }
        viewModel.updateByList(current);

    }

    private void SortByNameDESC() {
        List<MediaQueue> current = viewModel.getCurrentListVideoFavourite();
        current.sort((playlistItem, t1) -> {
            String name1 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            String name2 = MediaUtils.getMediaNameFromURI(requireContext(), Uri.parse(t1.getMediaUri()));
            return name2.compareTo(name1);
        });

        for (int i = 0; i < current.size(); i++) {
            MediaQueue item = current.get(i);
            item.setOrderSort(i);
        }
        viewModel.updateByList(current);
    }

    private void SortByDurationASC() {
        List<MediaQueue> current = viewModel.getCurrentListVideoFavourite();
        current.sort((playlistItem, t1) -> {
            Long dur1 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            Long dur2 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(t1.getMediaUri()));
            return dur1.compareTo(dur2);
        });

        for (int i = 0; i < current.size(); i++) {
            MediaQueue item = current.get(i);
            item.setOrderSort(i);
        }
        viewModel.updateByList(current);
    }

    private void SortByDurationDESC() {
        List<MediaQueue> current = viewModel.getCurrentListVideoFavourite();
        current.sort((playlistItem, t1) -> {
            Long dur1 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(playlistItem.getMediaUri()));
            Long dur2 = MediaUtils.getDurationFromUri(requireContext(), Uri.parse(t1.getMediaUri()));
            return dur2.compareTo(dur1);
        });

        for (int i = 0; i < current.size(); i++) {
            MediaQueue item = current.get(i);
            item.setOrderSort(i);
        }
        viewModel.updateByList(current);
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
    /**
     * Change display mode to grid.
     */
    private void setDisplayModeAsGrid() {
        binding.rcvVideoFavourite.setHasFixedSize(true);
        binding.rcvVideoFavourite.setLayoutManager(gridLayoutManager);
        adapter.setDisplayMode(DisplayMode.GRID);
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        binding.rcvVideoFavourite.setHasFixedSize(true);
        binding.rcvVideoFavourite.setLayoutManager(linearLayoutManager);

        adapter.setDisplayMode(DisplayMode.LIST);
    }

    @Override
    public void onNoteListChanged(List<PlaylistItem> customers) {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}