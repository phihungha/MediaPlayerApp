package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistRepository;
import com.example.mediaplayerapp.ui.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.data.playlist.PlaylistItem;
import com.example.mediaplayerapp.ui.playlist.PlaylistItemViewModel;
import com.example.mediaplayerapp.databinding.BottomSheetSongBinding;
import com.example.mediaplayerapp.databinding.SongDetailBinding;
import com.example.mediaplayerapp.ui.playlist.MediaQueueUtil;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.MediaUtils;
import com.example.mediaplayerapp.utils.SortOrder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class SongBottomSheet extends BottomSheetDialogFragment {
    private final Song currentSong;
    public SongBottomSheet(Song song) {
        currentSong = song;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomSheetSongBinding binding =
                BottomSheetSongBinding.inflate(inflater,container,false);
        binding.bottomSheetSongNameTextview.setText(currentSong.getTitle());

        LinearLayout SongDetail = binding.bottomSheetSongDetail;
        SongDetail.setOnClickListener(view -> {
            SongDetailBinding songDetailBinding = SongDetailBinding.inflate(inflater,container,false);
            songDetailBinding.songInfoTitle.setText(currentSong.getTitle());
            songDetailBinding.songInfoArtist.setText(currentSong.getArtistName());
            songDetailBinding.songInfoAlbum.setText(currentSong.getAlbumName());
            songDetailBinding.songInfoGenre.setText(currentSong.getGenre());
            songDetailBinding.songInfoDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(currentSong.getDuration()));
            songDetailBinding.songInfoTimeadded.setText(MediaTimeUtils.getFormattedTimeFromZonedDateTime(currentSong.getTimeAdded()));
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(songDetailBinding.getRoot()).show();
        });
        PlaylistViewModel playlistViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        // Get all playlists that contain videos
        //**** MUST **** do this before setOnClickListener for optionAddPlaylist, or else the first
        // time user clicks, dialog only shows title, NO ITEMS
        List<Playlist> allMusicPlaylists = new ArrayList<>();
        playlistViewModel.getAllPlaylists(PlaylistRepository.SortBy.NAME, SortOrder.ASC)
                .observe(
                    requireActivity(),
                    playlists -> {
                        for (Playlist playlist : playlists) {
                            if (!playlist.isVideo())
                                allMusicPlaylists.add(playlist);
                        }
                    }
                );

        binding.bottomSheetAddSongPlaylist.setOnClickListener(view -> {
            PlaylistItemViewModel PlaylistItemViewModel
                    = new ViewModelProvider(requireActivity()).get(PlaylistItemViewModel.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder
                    .setTitle("Choose a playlist: ")
                    .setItems(
                            allMusicPlaylists.stream().map(Playlist::getName).toArray(CharSequence[]::new),
                            (dialogInterface, i) -> {
                                PlaylistItem newPlaylistItem =
                                        new PlaylistItem(
                                            allMusicPlaylists.get(i).getId(),
                                            currentSong.getUri().toString()
                                        );
                                PlaylistItemViewModel.addPlaylistItem(newPlaylistItem);
                            })
                    .show();
        });

        binding.bottomSheetAddSongToFavorites.setOnClickListener(
                view -> MediaQueueUtil.insertSongToFavourite(
                        requireActivity().getApplication(),
                        currentSong.getUri().toString()
                )
        );

        binding.bottomSheetAddSongToWatchLater.setOnClickListener(
                view -> MediaQueueUtil.insertSongToWatchLater(
                        requireActivity().getApplication(),
                        currentSong.getUri().toString()
                )
        );

        return binding.getRoot();
    }
}