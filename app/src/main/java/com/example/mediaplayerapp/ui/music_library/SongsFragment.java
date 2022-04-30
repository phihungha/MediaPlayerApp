package com.example.mediaplayerapp.ui.music_library;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Song;
import com.example.mediaplayerapp.data.SongAdapter;
import com.example.mediaplayerapp.data.SongLoder;

import java.util.List;


public class SongsFragment extends Fragment {
    private SongAdapter songAdapter;
    private RecyclerView recyclerView;


    public SongsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_songs,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.sr);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        new LoadData().execute("");
        recyclerView.setHasFixedSize(true);
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }
    public class LoadData extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String...strings){
            if(getActivity()!=null)
            {
                songAdapter=new SongAdapter( new SongLoder().getAllSongs(getActivity()));
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            recyclerView.setAdapter(songAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}