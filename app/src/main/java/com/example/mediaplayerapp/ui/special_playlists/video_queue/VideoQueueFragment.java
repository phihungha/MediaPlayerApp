package com.example.mediaplayerapp.ui.special_playlists.video_queue;

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
import com.example.mediaplayerapp.data.special_playlists.MediaQueue;
import com.example.mediaplayerapp.data.special_playlists.MediaQueueViewModel;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.databinding.FragmentVideoQueueBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.special_playlists.PlaylistConstants;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueAdapter;
import com.example.mediaplayerapp.ui.special_playlists.MediaQueueDeleteDialog;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.ui.special_playlists.OnPlaylistItemListChangedListener;
import com.example.mediaplayerapp.utils.item_touch_helper.OnStartDragListener;
import com.example.mediaplayerapp.utils.item_touch_helper.SimpleItemTouchHelperCallback;

import java.util.List;

public class VideoQueueFragment extends Fragment implements OnStartDragListener, OnPlaylistItemListChangedListener {
    private FragmentVideoQueueBinding binding;
    private MediaQueueViewModel viewModel;
    private MediaQueueAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;

    private static final int GRID_MODE_COLUMN_NUM = 2;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

    public VideoQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoQueueBinding.inflate(inflater, container, false);
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
        adapter.setType(PlaylistConstants.TYPE_VIDEO_QUEUE);
        viewModel.getAllVideoQueue().observe(
                getViewLifecycleOwner(),
                mediaQueues -> adapter.submitList(mediaQueues)
        );

        setDisplayModeAsList();

        setUpRecyclerView();
        setAdapterListener();
    }

    private void setAdapterListener() {
        // item click
        adapter.setItemClickListener(this::ClickItem);
        //item delete
        adapter.setDeleteItemListener(this::DeleteItemQueue);
    }

    private void setUpRecyclerView(){
        adapter.setDragStartListener(this);
        adapter.setViewModel(viewModel);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.rcvVideoQueue);

        binding.rcvVideoQueue.setAdapter(adapter);
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
        inflater.inflate(R.menu.playlist_details_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_show_as_list_playlist_detail)
            ShowAsList();
        else if (item.getItemId() == R.id.action_show_as_grid_playlist_detail)
            ShowAsGrid();

        return super.onOptionsItemSelected(item);
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
        binding.rcvVideoQueue.setHasFixedSize(true);
        binding.rcvVideoQueue.setLayoutManager(gridLayoutManager);

        adapter.setDisplayMode(DisplayMode.GRID);
    }

    /**
     * Change display mode to list.
     */
    private void setDisplayModeAsList() {
        binding.rcvVideoQueue.setHasFixedSize(true);
        binding.rcvVideoQueue.setLayoutManager(linearLayoutManager);

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