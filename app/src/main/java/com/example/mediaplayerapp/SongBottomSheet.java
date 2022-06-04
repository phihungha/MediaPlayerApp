package com.example.mediaplayerapp;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.databinding.BottomSheetSongBinding;
import com.example.mediaplayerapp.databinding.FragmentSongBottomSheetBinding;
import com.example.mediaplayerapp.databinding.SongDetailBinding;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


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
        // Inflate the layout for this fragment
        return bottomSheetBinding.getRoot();
    }
}