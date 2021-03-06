package com.example.mediaplayerapp.ui.music_library.artist_tab;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.databinding.FragmentArtistDetailBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.song_tab.SongAdapter;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;

public class ArtistDetailFragment extends Fragment {
    private long currentArtistId;

    FragmentArtistDetailBinding binding;

    public ArtistDetailFragment() {
        // Required empty public constructor
    }

    public static ArtistDetailFragment newInstance() {
        return new ArtistDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentArtistId = ArtistDetailFragmentArgs.fromBundle(requireArguments()).getArtistId();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false);
        ArtistDetailsViewModel viewModel = new ViewModelProvider(this).get(ArtistDetailsViewModel.class);

        binding.artistDetailsSongList.setLayoutManager(new LinearLayoutManager(requireActivity()));

        SongAdapter adapter = new SongAdapter(requireContext(), orderIndex -> {
            Uri playbackUri = GetPlaybackUriUtils.forArtist(currentArtistId, orderIndex);
            MusicPlayerActivity.launchWithUri(requireActivity(), playbackUri);
        });
        adapter.setDisplayMode(DisplayMode.LIST);
        binding.artistDetailsSongList.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.artistDetailsSongList.setAdapter(adapter);

        binding.artistDetailsPlayAllSongs.setOnClickListener(v -> playAllSongs());

        viewModel.getArtist().observe(getViewLifecycleOwner(), this::setHeaderInfo);
        viewModel.getArtistSongs().observe(getViewLifecycleOwner(), adapter::updateSongs);
        viewModel.setCurrentArtistId(currentArtistId);

        return binding.getRoot();
    }

    private void setHeaderInfo(Artist artist) {
        binding.artistDetailsName.setText(artist.getArtistName());
        binding.artistDetailsSongNumber.setText(String.valueOf(artist.getNumberOfSongs()));
        binding.artistDetailsAlbumNumber.setText(String.valueOf(artist.getNumberOfAlbums()));
        setArtwork(artist.getThumbnailUri());
    }

    private void setArtwork(Uri uri) {
        Drawable artwork = MediaMetadataUtils.getThumbnail(
                requireContext(),
                uri,
                R.drawable.default_album_artwork);
        binding.artistDetailsArtwork.setImageDrawable(artwork);
        binding.artistDetailsSmallArtwork.setImageDrawable(artwork);
    }

    /**
     * Play all songs from this album by opening music player activity.
     */
    private void playAllSongs() {
        Uri uri = GetPlaybackUriUtils.forArtist(currentArtistId, 0);
        MusicPlayerActivity.launchWithUri(requireActivity(), uri);
    }
}