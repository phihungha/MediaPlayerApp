package com.example.mediaplayerapp.data.playlist;

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

    /**
     *  get All playlist from database
     * */
    public LiveData<List<Playlist>> getAllPlaylists(){
        return mAllPlaylists;
    }

    /**
     *  get All playlist from database
     * */
    public LiveData<List<Playlist>> getPlaylistWithID(int id){
        return mRepository.getPlaylistWithID(id);
    }

    /**
     *  get All playlist from database
     * */
    public LiveData<List<Playlist>> getAllPlaylistSearching(String text){
        return mRepository.getAllPlaylistSearching(text);
    }

    /**
     *  get All playlist from database sort by name asc
     * */
    public LiveData<List<Playlist>> sortPlaylistByNameASC(){
        return mRepository.sortPlaylistByNameASC();
    }

    /**
     *  get All playlist from database sort by name desc
     * */
    public LiveData<List<Playlist>> sortPlaylistByNameDESC(){
        return mRepository.sortPlaylistByNameDESC();
    }

    /**
     *  insert playlist to database
     * */
    public void insert(Playlist playlist){
        mRepository.insert(playlist);
    }

    /**
     *  update playlist from database
     * */
    public void update(Playlist playlist){
        mRepository.update(playlist);
    }

    /**
     *  delete playlist from database
     * */
    public void delete(Playlist playlist){
        mRepository.delete(playlist);
    }

    /**
     *  delete All playlist from database
     * */
    public void deleteAll(){
        mRepository.deleteAll();
    }
}
