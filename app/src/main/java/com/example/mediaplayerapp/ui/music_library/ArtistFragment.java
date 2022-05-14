package com.example.mediaplayerapp.ui.music_library;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Artist;
import com.example.mediaplayerapp.data.ArtistAdapter;
import com.example.mediaplayerapp.data.GridSpacingItemDecoration;
import com.example.mediaplayerapp.data.MusicLibraryRepository;


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
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            artists= (ArrayList<Artist>) MusicLibraryRepository.ArtistLoader.artisList(getActivity());
            artistAdapter = new ArtistAdapter(getContext(), artists);
            recyclerView.setAdapter(artistAdapter);
            if(getActivity()!=null)
            {
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,30,true));
            }
        }
        return view;
    }

}