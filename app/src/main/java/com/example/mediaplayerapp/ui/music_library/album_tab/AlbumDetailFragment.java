package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.FragmentAlbumDetailBinding;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MediaThumbnailUtils;
import com.example.mediaplayerapp.ui.music_library.song_tab.SongAdapter;

import java.io.IOException;

public class AlbumDetailFragment extends Fragment {
    private long currentAlbumId;
    private String currentAlbumNumberOfSongs;
    private String totalDuration;
    FragmentAlbumDetailBinding binding;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    public static AlbumDetailFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong("CURRENT_ALBUM_ID", id);
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentAlbumId = requireArguments().getLong("CURRENT_ALBUM_ID");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false);
        AlbumDetailsViewModel viewModel = new ViewModelProvider(this).get(AlbumDetailsViewModel.class);

        binding.albumDetailsSongList.setLayoutManager(new LinearLayoutManager(requireActivity()));

        SongAdapter adapter = new SongAdapter(requireContext(), orderIndex -> {
            Uri playbackUri = GetPlaybackUriUtils.forAlbum(currentAlbumId, orderIndex);
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        });
        adapter.setDisplayMode(DisplayMode.LIST);
        binding.albumDetailsSongList.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.albumDetailsSongList.setAdapter(adapter);

        binding.albumDetailsPlayAllSongs.setOnClickListener(v -> playAllSongs());

        viewModel.getAlbumName().observe(getViewLifecycleOwner(), s -> {
            binding.albumDetailsCollapsingLayout.setTitle(s);
            binding.albumDetailsName.setText(s);
        });
        viewModel.getNumberOfSongs().observe(getViewLifecycleOwner(), s -> {
            currentAlbumNumberOfSongs = s;
            updateDescription();
        });
        viewModel.getTotalDuration().observe(getViewLifecycleOwner(), s -> {
            totalDuration=s;
            updateDescription();
        });
        viewModel.getAlbumUri().observe(getViewLifecycleOwner(), this::updateArtwork);
        viewModel.getAlbumSongs().observe(getViewLifecycleOwner(), adapter::updateSongs);
        viewModel.setCurrentAlbumId(currentAlbumId);

        return binding.getRoot();
    }

    /**
     * Play all songs from this album by opening music player activity.
     */
    private void playAllSongs() {
        Uri uri = GetPlaybackUriUtils.forAlbum(currentAlbumId, 0);
        MusicPlayerActivity.launchWithUri(requireActivity(), uri);
    }

    /**
     * Update main and small album's artwork.
     * @param uri URI of the album
     */
    private void updateArtwork(Uri uri) {
        try {
            Bitmap artwork = MediaThumbnailUtils.getThumbnailFromUri(requireContext(), uri);
            binding.albumDetailsArtwork.setImageBitmap(artwork);
            binding.albumDetailsSmallArtwork.setImageBitmap(artwork);
        } catch (IOException e) {
            binding.albumDetailsSmallArtwork.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(),
                            R.drawable.default_album_artwork));
            binding.albumDetailsArtwork.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(),
                            R.drawable.default_album_artwork));
        }
    }

    /**
     * Update album's description.
     */
    private void updateDescription() {
        String description = currentAlbumNumberOfSongs + " song(s),"
                +" total duration: "+totalDuration;
        binding.albumDetailsDescription.setText(description);
    }
}