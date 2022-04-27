package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.data.PlaylistRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {
    private PlaylistRepository mRepository;
    private final LiveData<List<Playlist>> mAllPlaylists;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        mRepository=new PlaylistRepository(application);
        mAllPlaylists= mRepository.getAllPlaylists();
    }

    LiveData<List<Playlist>> getAllPlaylists(){
        return mAllPlaylists;
    }

    public void insert(Playlist playlist){
        mRepository.insert(playlist);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }
}
