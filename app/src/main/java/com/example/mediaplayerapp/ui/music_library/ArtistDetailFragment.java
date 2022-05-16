package com.example.mediaplayerapp.ui.music_library;

import android.annotation.SuppressLint;
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
import com.example.mediaplayerapp.data.music_library.Artist;
import com.example.mediaplayerapp.data.music_library.ArtistRepository;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongRepository;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


public class ArtistDetailFragment extends Fragment {
    private long artistId;

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
        assert getArguments() != null;
        artistId = getArguments().getLong("_ID");
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArtistRepository artistRepository = new ArtistRepository(requireActivity().getApplicationContext());
        SongRepository songRepository = new SongRepository(requireActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);
        TextView anaam = view.findViewById(R.id.artistnaam);
        TextView ade = view.findViewById(R.id.artistDetails);
        ImageView img = view.findViewById(R.id.bigartist);
        ImageView img2 = view.findViewById(R.id.artistimg);
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.artistcollapsinglayout);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //set artist detail
        Artist artist = artistRepository.getArtist(artistId);
        collapsingToolbarLayout.setTitle(artist.ArtistName);
        anaam.setText(artist.ArtistName);
        Glide.with(getContext()).load(artistId).skipMemoryCache(true).into(img);
        Glide.with(getContext()).load(artistId).skipMemoryCache(true).into(img2);
        //set song list of artist
        List<Song> songList = songRepository.getAllSongsFromArtist(artistId);
        ade.setText(" songs: " + songList.size());
        SongAdapter adapter = new SongAdapter(getActivity(), songList);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        return view;
    }
}