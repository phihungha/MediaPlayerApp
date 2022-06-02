package com.example.mediaplayerapp.ui.playlist.media_queue.music_fav;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.databinding.FragmentMusicFavouriteBinding;
import com.example.mediaplayerapp.databinding.FragmentVideoQueueBinding;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.ui.playlist.media_queue.MediaQueueAdapter;
import com.example.mediaplayerapp.ui.playlist.media_queue.MediaQueueDeleteDialog;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;

import java.util.List;


public class MusicFavouriteFragment extends Fragment {
    private FragmentMusicFavouriteBinding binding;
    private MediaQueueViewModel viewModel;
    private MediaQueueAdapter adapter;

    public MusicFavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMusicFavouriteBinding.inflate(inflater, container, false);
        viewModel=new ViewModelProvider(this).get(MediaQueueViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter=new MediaQueueAdapter(new MediaQueueAdapter.MediaQueueDiff());
        adapter.setContext(requireContext());
        adapter.setApplication(requireActivity().getApplication());

        binding.rcvMusicFavourite.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMusicFavourite.setAdapter(adapter);

        viewModel.getAllMusicFavourite().observe(
                getViewLifecycleOwner(),
                new Observer<List<MediaQueue>>() {
                    @Override
                    public void onChanged(List<MediaQueue> mediaQueues) {
                        adapter.submitList(mediaQueues);
                    }
                }
        );
        setAdapterListener();
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}