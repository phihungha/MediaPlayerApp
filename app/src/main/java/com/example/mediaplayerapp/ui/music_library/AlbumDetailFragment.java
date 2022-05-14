package com.example.mediaplayerapp.ui.music_library;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Album;
import com.example.mediaplayerapp.data.MusicLibraryRepository;
import com.example.mediaplayerapp.data.Song;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;


public class AlbumDetailFragment extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private long album_id;
    private ArrayList<Song> songList = new ArrayList<>();
    private Album album;
    private TextView anaam, ade;
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    public AlbumDetailFragment() {
        // Required empty public constructor
    }


    public static AlbumDetailFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong("_ID", id);
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        album_id = getArguments().getLong("_ID");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_album_detail, container, false);
        anaam = rootView.findViewById(R.id.atrnaam);
        ade = rootView.findViewById(R.id.albumDetails);
        collapsingToolbarLayout = rootView.findViewById(R.id.collapsed_layout);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        album = new MusicLibraryRepository.AlbumLoader().getAlbum(getActivity(), album_id);
        collapsingToolbarLayout.setTitle(album.albumName);
        anaam.setText(album.albumName);
        ade.setText("songs: " + album.numSong);
        songList = MusicLibraryRepository.AlbumSongLoder.getAllAlbumSongs(getActivity(), album_id);
        adapter = new SongAdapter(getActivity(), songList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return rootView;
    }
}