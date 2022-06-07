package com.example.mediaplayerapp.ui.music_library.song_tab;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistViewModel;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.databinding.BottomSheetSongBinding;
import com.example.mediaplayerapp.databinding.FragmentSongBottomSheetBinding;
import com.example.mediaplayerapp.databinding.SongDetailBinding;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.MediaUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class SongBottomSheet extends BottomSheetDialogFragment {
    private final Song currentsong;
    public SongBottomSheet(Song song) {
        currentsong=song;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSongBottomSheetBinding bottomSheetBinding = FragmentSongBottomSheetBinding.inflate(inflater,container,false);
        bottomSheetBinding.bottomSheetSongNameTextview.setText(currentsong.getTitle());
        LinearLayout SongDetail = bottomSheetBinding.bottomSheetSongDetail;
        SongDetail.setOnClickListener(view -> {
            SongDetailBinding songDetailBinding = SongDetailBinding.inflate(inflater,container,false);
            songDetailBinding.songInfoTitle.setText(currentsong.getTitle());
            songDetailBinding.songInfoArtist.setText(currentsong.getArtistName());
            songDetailBinding.songInfoAlbum.setText(currentsong.getAlbumName());
            songDetailBinding.songInfoGenre.setText(currentsong.getGenre());
            songDetailBinding.songInfoDuration.setText(MediaTimeUtils.getFormattedTimeFromLong(currentsong.getDuration()));
            songDetailBinding.songInfoTimeadded.setText(MediaTimeUtils.getFormattedTimeFromZonedDateTime(currentsong.getTimeAdded()));
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(songDetailBinding.getRoot()).show();
        });
        PlaylistViewModel playlistViewModel
                = new ViewModelProvider(requireActivity()).get(PlaylistViewModel.class);

        // Get all playlists that contain videos
        //**** MUST **** do this before setOnClickListener for optionAddPlaylist, or else the first
        // time user clicks, dialog only shows title, NO ITEMS
        List<Playlist> allMusicPlaylists = new ArrayList<>();
        playlistViewModel.getAllPlaylists().observe(
                requireActivity(),
                playlists -> {
                    for (Playlist playlist : playlists) {
                        if (!playlist.isVideo())
                            allMusicPlaylists.add(playlist);
                    }
                });
        LinearLayout optionAdd = bottomSheetBinding.bottomSheetAddSongPlaylist;
        optionAdd.setOnClickListener(view -> {
            PlaylistItemViewModel PlaylistItemViewModel
                    = new ViewModelProvider(requireActivity()).get(PlaylistItemViewModel.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder
                    .setTitle("Choose a playlist: ")
                    .setItems(
                            allMusicPlaylists.stream().map(Playlist::getName).toArray(CharSequence[]::new),
                            (dialogInterface, i) -> {
                                PlaylistItem newPlaylistItem = new PlaylistItem(
                                        allMusicPlaylists.get(i).getId(),
                                        currentsong.getUri().toString(),
                                        MediaUtils.generateOrderSort());
                                PlaylistItemViewModel.insert(newPlaylistItem);
                            })
                    .show();
        });

        return bottomSheetBinding.getRoot();
    }
}