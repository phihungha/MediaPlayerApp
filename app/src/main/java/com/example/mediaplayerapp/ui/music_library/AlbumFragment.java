package com.example.mediaplayerapp.ui.music_library;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Album;
import com.example.mediaplayerapp.data.AlbumAdapter;
import com.example.mediaplayerapp.data.GridSpacingItemDecoration;
import com.example.mediaplayerapp.data.MusicLibraryRepository;

import java.util.ArrayList;


public class AlbumFragment extends Fragment {
    private ArrayList<Album> albums= new ArrayList<Album>();
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    public AlbumFragment() {
        // Required empty public constructor
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null)
        {
            view = inflater.inflate(R.layout.fragment_album, container, false);
            recyclerView = view.findViewById(R.id.ar);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            albums= (ArrayList<Album>) MusicLibraryRepository.AlbumLoader.albumList(getActivity());
            albumAdapter = new AlbumAdapter(getContext(),albums);
            recyclerView.setAdapter(albumAdapter);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        // Inflate the layout for this fragment
        return view;
    }
}