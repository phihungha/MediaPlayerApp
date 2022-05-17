package com.example.mediaplayerapp.ui.music_library.artist_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.databinding.FragmentArtistDetailBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.song_tab.SongAdapter;

public class ArtistDetailFragment extends Fragment {
    private long currentArtistId;
    private String currentArtistNumberOfSongs;
    private String currentArtistNumberOfAlbums;

    FragmentArtistDetailBinding binding;

    public ArtistDetailFragment() {
        // Required empty public constructor
    }

    public static ArtistDetailFragment newInstance(long artist_id) {
        Bundle args = new Bundle();
        args.putLong("CURRENT_ARTIST_ID", artist_id);
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        currentArtistId = requireArguments().getLong("CURRENT_ARTIST_ID");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false);
        ArtistDetailsViewModel viewModel = new ViewModelProvider(this).get(ArtistDetailsViewModel.class);

        binding.artistDetailsSongList.setLayoutManager(new LinearLayoutManager(requireActivity()));

        SongAdapter adapter = new SongAdapter(requireContext());
        adapter.setDisplayMode(DisplayMode.LIST);
        binding.artistDetailsSongList.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.artistDetailsSongList.setAdapter(adapter);

        viewModel.getArtistName().observe(getViewLifecycleOwner(), s -> {
            binding.artistDetailsCollapsingLayout.setTitle(s);
            binding.artistDetailsName.setText(s);
        });
        viewModel.getNumberOfSongs().observe(getViewLifecycleOwner(), s -> {
            currentArtistNumberOfSongs = s;
            updateDescription();
        });
        viewModel.getNumberOfAlbums().observe(getViewLifecycleOwner(), s -> {
            currentArtistNumberOfAlbums = s;
            updateDescription();
        });
        viewModel.getArtistSongs().observe(getViewLifecycleOwner(), adapter::updateSongs);
        viewModel.setCurrentArtistId(currentArtistId);

        return binding.getRoot();
    }

    /**
     * Update artist's description.
     */
    private void updateDescription() {
        String description = "This artist has "
                                + currentArtistNumberOfSongs
                                + " song(s) and "
                                + currentArtistNumberOfAlbums
                                + " album(s)";
        binding.artistDetailsDescription.setText(description);
    }
}