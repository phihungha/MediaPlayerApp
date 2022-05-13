package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.databinding.FragmentMediaQueueBinding;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;

import java.util.List;

public class MediaQueueFragment extends Fragment implements View.OnClickListener {
    private FragmentMediaQueueBinding binding;
    private MediaQueueAdapter adapter;
    private MediaQueueViewModel mediaQueueViewModel;

    public MediaQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMediaQueueBinding.inflate(inflater, container, false);
        mediaQueueViewModel = new ViewModelProvider(this).get(MediaQueueViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MediaQueueAdapter(new MediaQueueAdapter.MediaQueueDiff());
        adapter.setContext(getContext());
        binding.rcvQueue.setAdapter(adapter);
        setListeners();
        mediaQueueViewModel.getAllMediaQueue().observe(
                getViewLifecycleOwner(),
                new Observer<List<MediaQueue>>() {
                    @Override
                    public void onChanged(List<MediaQueue> mediaQueues) {
                        adapter.submitList(mediaQueues);
                    }
                }
        );
    }

    private void setListeners(){
        binding.layoutSaveToPlaylist.setOnClickListener(this);
        binding.layoutClearQueue.setOnClickListener(this);
        adapter.setDeleteItemListener(new IOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                DeleteItemQueue(view,position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_saveToPlaylist:
                AddToPlayList();
                break;

            case R.id.layout_clearQueue:
                ClearQueue();
                break;
        }
    }

    private void DeleteItemQueue(View view,int position) {
        MediaQueue mediaQueue=adapter.getItemAt(position);
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(mediaQueue);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_QUEUE);
    }

    private void ClearQueue() {
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(null);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_ALL_QUEUE);
    }

    private void AddToPlayList() {
        PlaylistViewModel playlistViewModel=new ViewModelProvider(this).get(PlaylistViewModel.class);

    }
}