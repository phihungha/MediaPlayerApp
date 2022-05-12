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

import com.example.mediaplayerapp.databinding.FragmentMediaQueueBinding;

import java.util.List;

public class MediaQueueFragment extends Fragment {
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
}