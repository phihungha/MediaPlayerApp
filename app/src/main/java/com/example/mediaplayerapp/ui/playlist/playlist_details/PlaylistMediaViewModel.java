package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaylistMediaViewModel extends AndroidViewModel{
    private PlaylistMediaRepository mRepository;
    private final LiveData<List<PlaylistMedia>> mAllMedias;

    public PlaylistMediaViewModel(@NonNull Application application) {
        super(application);
        mRepository=new PlaylistMediaRepository(application);
        mAllMedias =mRepository.getAllPlaylistMedias();
    }

    LiveData<List<PlaylistMedia>> getAllPlaylistMediasWithID(int id){
        return mRepository.getAllPlaylistMediasWithID(id);
    }

    LiveData<List<PlaylistMedia>> getAllPlaylistMedias(){
        return mAllMedias;
    }

    public void insert(PlaylistMedia media){
        mRepository.insert(media);
    }

    public void update(PlaylistMedia media){
        mRepository.update(media);
    }
    public void delete(PlaylistMedia media){
        mRepository.delete(media);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }
}
