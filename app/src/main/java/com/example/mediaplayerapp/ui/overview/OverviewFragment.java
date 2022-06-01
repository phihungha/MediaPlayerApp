package com.example.mediaplayerapp.ui.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.databinding.FragmentOverviewBinding;

public class OverviewFragment extends Fragment {
    private OverviewItemAdapter recentVideosItemAdapter;
    private OverviewItemAdapter mostWatchedVideosItemAdapter;
    private OverviewItemAdapter recentSongsItemAdapter;
    private OverviewItemAdapter mostListenedSongsItemAdapter;
    private FragmentOverviewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        // Initialize recent videos recyclerview
        recentVideosItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.VIDEO, MediaLayoutType.SMALL, requireActivity());
        RecyclerView recentVideosRecyclerView = binding.recentVideosRecyclerview;
        setupRecyclerView(recentVideosRecyclerView, recentVideosItemAdapter);

        // Initialize most watched videos recyclerview
        mostWatchedVideosItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.VIDEO, MediaLayoutType.BIG, requireActivity());
        RecyclerView mostWatchedVideosRecyclerView = binding.mostWatchedVideosRecyclerview;
        setupRecyclerView(mostWatchedVideosRecyclerView, mostWatchedVideosItemAdapter);

        // Initialize recent songs recyclerview
        recentSongsItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.SONG, MediaLayoutType.SMALL, requireActivity());
        RecyclerView recentSongsRecyclerView = binding.recentSongsRecyclerview;
        setupRecyclerView(recentSongsRecyclerView, recentSongsItemAdapter);

        // Initialize most listened songs recyclerview
        mostListenedSongsItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.SONG, MediaLayoutType.BIG, requireActivity());
        RecyclerView mostListenedSongsRecyclerView = binding.mostListenedSongsRecyclerview;
        setupRecyclerView(mostListenedSongsRecyclerView, mostListenedSongsItemAdapter);

        implementOverviewViewModel();
        return binding.getRoot();
    }

    /**
     * A method wrapping around recycler views setup for better readability
     */
    private void setupRecyclerView(RecyclerView recyclerView, OverviewItemAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
    }

    /**
     * A method wrapping around implementation of OverviewViewModel for better readability
     */
    private void implementOverviewViewModel() {
        OverviewViewModel overviewViewModel
                = new ViewModelProvider(requireActivity()).get(OverviewViewModel.class);

        int DEFAULT_MEDIA_SHOWN_COUNT = 8;

        overviewViewModel.getRecentVideos(DEFAULT_MEDIA_SHOWN_COUNT).observe(
                requireActivity(),
                mediaPlaybackInfoList -> recentVideosItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.getMostWatchedVideos(DEFAULT_MEDIA_SHOWN_COUNT).observe(
                requireActivity(),
                mediaPlaybackInfoList -> mostWatchedVideosItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.getRecentSongs(DEFAULT_MEDIA_SHOWN_COUNT).observe(
                requireActivity(),
                mediaPlaybackInfoList -> recentSongsItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.getMostListenedSongs(DEFAULT_MEDIA_SHOWN_COUNT).observe(
                requireActivity(),
                mediaPlaybackInfoList -> mostListenedSongsItemAdapter.submitList(mediaPlaybackInfoList));
    }

    public enum MediaType {
        VIDEO,
        SONG
    }

    /**
     * Indicating whether this recyclerview will use the layout file for item_small or item_big
     */
    public enum MediaLayoutType {
        BIG,
        SMALL
    }
}