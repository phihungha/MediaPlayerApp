package com.example.mediaplayerapp.ui.music_library.album_tab;

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
import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.databinding.FragmentAlbumDetailBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.music_library.song_tab.SongAdapter;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.utils.GetPlaybackUriUtils;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.example.mediaplayerapp.utils.MediaTimeUtils;

import java.util.List;

public class AlbumDetailFragment extends Fragment {

    private long currentAlbumId;

    FragmentAlbumDetailBinding binding;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    public static AlbumDetailFragment newInstance() {
        return new AlbumDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentAlbumId = AlbumDetailFragmentArgs.fromBundle(requireArguments()).getAlbumId();
    }

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
        binding.albumDetailsSongList.addItemDecoration(
                new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        );
        binding.albumDetailsSongList.setAdapter(adapter);

        binding.albumDetailsPlayAllSongs.setOnClickListener(v -> playAllSongs());

        viewModel.getAlbum(currentAlbumId).observe(getViewLifecycleOwner(), this::setHeaderInfo);
        viewModel.getAlbumSongs(currentAlbumId).observe(getViewLifecycleOwner(),
                songs -> {
            adapter.updateSongs(songs);
            setTotalDuration(songs);
        });

        return binding.getRoot();
    }

    /**
     * Play all songs from this album by opening music player activity.
     */
    private void playAllSongs() {
        Uri uri = GetPlaybackUriUtils.forAlbum(currentAlbumId, 0);
        MusicPlayerActivity.launchWithUri(requireActivity(), uri);
    }

    private void setHeaderInfo(Album album) {
        binding.albumDetailsName.setText(album.getAlbumName());
        binding.albumDetailsArtist.setText(album.getArtistName());
        if (album.getYear() != 0)
            binding.albumDetailsReleaseYear.setText(String.valueOf(album.getYear()));
        binding.albumDetailsSongNumber.setText(String.valueOf(album.getNumberOfSongs()));
        setArtwork(album.getUri());
    }

    private void setTotalDuration(List<Song> songs) {
        long totalDurationLong = songs.stream().mapToLong(Song::getDuration).sum();
        binding.albumDetailsTotalDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(totalDurationLong));
    }

    /**
     * Update main and small album's artwork.
     * @param uri URI of the album
     */
    private void setArtwork(Uri uri) {
        Drawable artwork = MediaMetadataUtils.getThumbnail(
                requireContext(),
                uri,
                R.drawable.default_album_artwork
        );
        binding.albumDetailsArtwork.setImageDrawable(artwork);
        binding.albumDetailsSmallArtwork.setImageDrawable(artwork);
    }
}