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
    private FragmentOverviewBinding binding;
    private RecyclerView recentVideosRecyclerView;
    private RecyclerView mostWatchedVideosRecyclerView;
    private OverviewItemAdapter recentVideosItemAdapter;
    private OverviewItemAdapter mostWatchedVideosItemAdapter;
    private OverviewViewModel overviewViewModel;
    private String mParam1;
    private String mParam2;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        recentVideosItemAdapter = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff());

        recentVideosRecyclerView = binding.recentVideosRecyclerview;
        recentVideosRecyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));

        recentVideosRecyclerView.setAdapter(recentVideosItemAdapter);

        mostWatchedVideosItemAdapter
                = new OverviewItemAdapter(new OverviewItemAdapter.MediaPlaybackInfoDiff());
        mostWatchedVideosRecyclerView = binding.mostWatchedVideosRecyclerview;
        mostWatchedVideosRecyclerView.setLayoutManager(new LinearLayoutManager
                (binding.getRoot().getContext(), LinearLayoutManager.HORIZONTAL, false));
        mostWatchedVideosRecyclerView.setAdapter(mostWatchedVideosItemAdapter);


        overviewViewModel = new ViewModelProvider(requireActivity()).get(OverviewViewModel.class);
        overviewViewModel.get5RecentVideos().observe(
                requireActivity(),
                mediaPlaybackInfoList -> recentVideosItemAdapter.submitList(mediaPlaybackInfoList));

        overviewViewModel.get5MostWatchedVideos().observe(
                requireActivity(),
                mediaPlaybackInfoList -> mostWatchedVideosItemAdapter.submitList(mediaPlaybackInfoList));

        return binding.getRoot();
    }
}