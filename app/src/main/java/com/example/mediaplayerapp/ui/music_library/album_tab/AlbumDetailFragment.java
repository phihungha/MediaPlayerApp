package com.example.mediaplayerapp.ui.music_library.album_tab;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.music_library.Album;
import com.example.mediaplayerapp.data.music_library.AlbumRepository;
import com.example.mediaplayerapp.data.music_library.Song;
import com.example.mediaplayerapp.data.music_library.SongRepository;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;


public class AlbumDetailFragment extends Fragment {
    private long album_id;

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
        assert getArguments() != null;
        album_id = getArguments().getLong("_ID");
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlbumRepository albumRepository = new AlbumRepository(requireActivity().getApplicationContext());
        SongRepository songRepository = new SongRepository(requireActivity().getApplicationContext());
        View rootView=inflater.inflate(R.layout.fragment_album_detail, container, false);
        TextView anaam = rootView.findViewById(R.id.album_details_name);
        TextView ade = rootView.findViewById(R.id.album_details_description);
        ImageView img = rootView.findViewById(R.id.album_details_artwork);
        ImageView img2 = rootView.findViewById(R.id.album_details_small_artwork);
        CollapsingToolbarLayout collapsingToolbarLayout = rootView.findViewById(R.id.collapsed_layout);
        RecyclerView recyclerView = rootView.findViewById(R.id.album_details_song_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Album album = albumRepository.getAlbum(album_id);
        //set album detail
        collapsingToolbarLayout.setTitle(album.albumName);
        anaam.setText(album.albumName);
        ade.setText("songs: " + album.numberOfSongs);
        Glide.with(getContext()).load(getImage(album_id)).skipMemoryCache(true).into(img);
        Glide.with(getContext()).load(getImage(album_id)).skipMemoryCache(true).into(img2);
        //set song list of a album
        List<Song> songList = songRepository.getAllSongsFromAlbum(album_id);
//        SongAdapter adapter = new SongAdapter(getActivity(), songList);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//        recyclerView.setAdapter(adapter);
        return rootView;
    }
    public static Uri getImage(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }
}