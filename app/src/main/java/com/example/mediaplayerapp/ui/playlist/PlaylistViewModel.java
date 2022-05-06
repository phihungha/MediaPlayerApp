package com.example.mediaplayerapp.ui.playlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.PlaylistRepository;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {
    private PlaylistRepository mRepository;
    private final LiveData<List<Playlist>> mAllPlaylists;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        mRepository=new PlaylistRepository(application);
        mAllPlaylists= mRepository.getAllPlaylists();
    }

    public LiveData<List<Playlist>> getAllPlaylists(){
        return mAllPlaylists;
    }

/*    public LiveData<List<Playlist>> getPlaylistWithID(int id){
        return mRepository.getPlaylistWithID(id);
    }*/

    public void insert(Playlist playlist){
        mRepository.insert(playlist);
    }

    public void update(Playlist playlist){
        mRepository.update(playlist);
    }
    public void delete(Playlist playlist){
        mRepository.delete(playlist);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }
}
