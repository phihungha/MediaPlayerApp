package com.example.mediaplayerapp.ui.music_library;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Artist;
import com.example.mediaplayerapp.data.MusicLibraryRepository;
import com.example.mediaplayerapp.data.Song;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;


public class ArtistDetailFragment extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private long artistId;
    private Artist artist;
    private TextView anaam, ade;
    private ImageView img, img2;
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private ArrayList<Song> songList = new ArrayList<>();

    public ArtistDetailFragment() {
        // Required empty public constructor
    }

    public static ArtistDetailFragment newInstance(long artist_id) {

        Bundle args = new Bundle();
        args.putLong("_ID", artist_id);
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        artistId = getArguments().getLong("_ID");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);
        anaam = view.findViewById(R.id.artistnaam);
        ade = view.findViewById(R.id.artistDetails);
        img = view.findViewById(R.id.bigartist);
        img2 = view.findViewById(R.id.artistimg);
        collapsingToolbarLayout = view.findViewById(R.id.artistcollapsinglayout);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //set artist detail
        artist = MusicLibraryRepository.ArtistLoader.getArtis(getActivity(),artistId);
        collapsingToolbarLayout.setTitle(artist.ArtistName);
        anaam.setText(artist.ArtistName);
        Glide.with(getContext()).load(artistId).skipMemoryCache(true).into(img);
        Glide.with(getContext()).load(artistId).skipMemoryCache(true).into(img2);
        //set song list of artist
        songList = (ArrayList<Song>) MusicLibraryRepository.ArtistSongLoader.getAllArtistSongs(getActivity(), artistId);
        ade.setText(" songs: " + songList.size());
        adapter = new SongAdapter(getActivity(), songList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        return view;
    }
}