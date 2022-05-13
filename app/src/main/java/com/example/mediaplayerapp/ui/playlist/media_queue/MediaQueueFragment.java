package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMedia;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMediaViewModel;
import com.example.mediaplayerapp.databinding.FragmentMediaQueueBinding;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
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

    private void setListeners() {
        binding.layoutClearQueue.setOnClickListener(this);
        binding.layoutPlayQueue.setOnClickListener(this);

        adapter.setDeleteItemListener(new IOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                DeleteItemQueue(view, position);
            }
        });

        adapter.setItemClickListener(new IOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ClickItem(view, position);
            }
        });
    }

    private void ClickItem(View view, int position) {
        MediaQueue media = adapter.getItemAt(position);
        Uri uri = Uri.parse(media.getMediaUri());
        /**
         *
         *              Click to play item queue
         *
         *
         *
         * */
        makeToast("Play item at " + position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_clearQueue:
                ClearQueue();
                break;

            case R.id.layout_playQueue:
                PlayQueue();
                break;
        }
    }

    private void PlayQueue() {
        List<MediaQueue> listMedia = adapter.getCurrentList();
        List<Uri> uriList = new ArrayList<>();
        listMedia.forEach(item -> {
            Uri uri = Uri.parse(item.getMediaUri());
            uriList.add(uri);
        });
        /**
         *
         *              Click to play item queue
         *
         *
         *
         * */
        makeToast("Play all");

    }

    private void DeleteItemQueue(View view, int position) {
        MediaQueue mediaQueue = adapter.getItemAt(position);
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(mediaQueue);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_QUEUE);
    }

    private void ClearQueue() {
        MediaQueueDeleteDialog dialog = MediaQueueDeleteDialog.newInstance(null);
        dialog.show(getParentFragmentManager(), PlaylistConstants.TAG_DELETE_DIALOG_ALL_QUEUE);
    }

    private void makeToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }
}