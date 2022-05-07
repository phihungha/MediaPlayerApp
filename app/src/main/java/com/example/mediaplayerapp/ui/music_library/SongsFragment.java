package com.example.mediaplayerapp.ui.music_library;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Song;
import com.example.mediaplayerapp.data.SongAdapter;

import java.util.ArrayList;


public class SongsFragment extends Fragment {
    private SongAdapter songAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Song> SongList = new ArrayList<Song>();
    private Song s;


    public SongsFragment() {
        // Required empty public constructor
    }


    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view==null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_songs, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.sr);
            LinearLayoutManager linearLayoutManager = new
                    LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
            String[] projection = {
                    MediaStore.Audio.Media._ID,//0
                    MediaStore.Audio.Media.TITLE,//1
                    MediaStore.Audio.Media.ALBUM_ID,//2
                    MediaStore.Audio.Media.ALBUM,//3
                    MediaStore.Audio.Media.ARTIST_ID,//4
                    MediaStore.Audio.Media.ARTIST,//5
                    MediaStore.Audio.Media.DURATION,//6
                    MediaStore.Audio.Media.TRACK//7
            };
            Cursor cursor = getActivity().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    null);
            while (cursor.moveToNext()) {
                SongList.add(convertToSong(cursor));
            }
            songAdapter = new SongAdapter(getContext(), SongList);
            recyclerView.setAdapter(songAdapter);
        }
        return view;
    }
    private Song convertToSong(Cursor cursor) {
        Song song = new Song();
        song.setId(cursor.getLong(0));
        song.setTitle(cursor.getString(1));
        song.setAlbumId(cursor.getLong(2));
        song.setAlbum(cursor.getString(3));
        song.setArtistId(cursor.getLong(4));
        song.setArtist(cursor.getString(5));
        song.setDuration(cursor.getLong(6));
        song.setTrackNumber(cursor.getInt(7));
        return song;
    }

}