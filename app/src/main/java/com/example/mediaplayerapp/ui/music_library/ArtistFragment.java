package com.example.mediaplayerapp.ui.music_library;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Artist;
import com.example.mediaplayerapp.data.ArtistAdapter;
import com.example.mediaplayerapp.data.GridSpacingItemDecoration;


import java.util.ArrayList;


public class ArtistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private ArrayList<Artist> artists = new ArrayList<Artist>();
    private Artist artist;
    public ArtistFragment() {
        // Required empty public constructor
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_artist, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.arr);
            LinearLayoutManager linearLayoutManager = new
                    LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            String[] mProjection =
                    {
                            MediaStore.Audio.Artists._ID,
                            MediaStore.Audio.Artists.ARTIST,
                            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
                    };

            Cursor artistCursor = getActivity().getContentResolver().query(
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    mProjection,
                    null,
                    null,
                    MediaStore.Audio.Artists.ARTIST + " ASC");
            while (artistCursor.moveToNext()) {
                artists.add(convertToArtist(artistCursor));
            }
            artistAdapter = new ArtistAdapter(getContext(), artists);
            recyclerView.setAdapter(artistAdapter);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        return view;
    }
    private Artist convertToArtist(Cursor cursor) {
        Artist artist = new Artist();
        artist.setId(cursor.getLong(0));
        artist.setName(cursor.getString(1));
        artist.setNum_tracks(cursor.getLong(2));
        artist.setNum_albums(cursor.getLong(3));
        return artist;
    }
}