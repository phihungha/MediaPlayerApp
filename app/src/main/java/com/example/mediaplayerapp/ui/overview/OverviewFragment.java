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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OverviewItemAdapter recentVideosItemAdapter;
    private OverviewItemAdapter mostWatchedVideosItemAdapter;
    private OverviewItemAdapter recentSongsItemAdapter;
    private OverviewItemAdapter mostListenedSongsItemAdapter;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOverviewBinding binding =
                FragmentOverviewBinding.inflate(inflater, container, false);

        // Initialize recent videos recyclerview
        recentVideosItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.VIDEO, MediaLayoutType.SMALL, requireActivity());
        RecyclerView recentVideosRecyclerView = binding.recentVideosRecyclerview;
        recentVideosRecyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentVideosRecyclerView.setAdapter(recentVideosItemAdapter);
        recentVideosRecyclerView.setHasFixedSize(false);

        // Initialize most watched videos recyclerview
        mostWatchedVideosItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.VIDEO, MediaLayoutType.BIG, requireActivity());
        RecyclerView mostWatchedVideosRecyclerView = binding.mostWatchedVideosRecyclerview;
        mostWatchedVideosRecyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostWatchedVideosRecyclerView.setAdapter(mostWatchedVideosItemAdapter);
        mostWatchedVideosRecyclerView.setHasFixedSize(false);

        // Initialize recent songs recyclerview
        recentSongsItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.SONG, MediaLayoutType.SMALL, requireActivity());
        RecyclerView recentSongsRecyclerView = binding.recentSongsRecyclerview;
        recentSongsRecyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentSongsRecyclerView.setAdapter(recentSongsItemAdapter);
        recentSongsRecyclerView.setHasFixedSize(false);

        // Initialize most listened songs recyclerview
        mostListenedSongsItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff(),
                MediaType.SONG, MediaLayoutType.BIG, requireActivity());
        RecyclerView mostListenedSongsRecyclerView = binding.mostListenedSongsRecyclerview;
        mostListenedSongsRecyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostListenedSongsRecyclerView.setAdapter(mostListenedSongsItemAdapter);
        mostListenedSongsRecyclerView.setHasFixedSize(false);

        OverviewViewModel overviewViewModel
                = new ViewModelProvider(requireActivity()).get(OverviewViewModel.class);
        overviewViewModel.get5RecentVideos().observe(
                requireActivity(),
                mediaPlaybackInfoList -> recentVideosItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.get5MostWatchedVideos().observe(
                requireActivity(),
                mediaPlaybackInfoList -> mostWatchedVideosItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.get5RecentSongs().observe(
                requireActivity(),
                mediaPlaybackInfoList -> recentSongsItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.get5MostListenedSongs().observe(
                requireActivity(),
                mediaPlaybackInfoList -> mostListenedSongsItemAdapter.submitList(mediaPlaybackInfoList));

        return binding.getRoot();
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