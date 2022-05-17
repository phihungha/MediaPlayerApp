package com.example.mediaplayerapp.data.playlist.playlist_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MediaItemViewModel extends AndroidViewModel{
    private final MediaItemRepository mRepository;
    private final LiveData<List<MediaItem>> mAllMedias;

    public MediaItemViewModel(@NonNull Application application) {
        super(application);
        mRepository=new MediaItemRepository(application);
        mAllMedias =mRepository.getAllPlaylistMedias();
    }

    /**
     *  get all MediaItem object with id of playlist from database to display
     * */
    public LiveData<List<MediaItem>> getAllPlaylistMediasWithID(int id){
        return mRepository.getAllPlaylistMediasWithID(id);
    }

    /**
     *  get all MediaItem object with name from database to display
     * */
    public LiveData<List<MediaItem>> getAllMediaSearching(String text){
        return mRepository.getAllMediaSearching(text);
    }

    /**
     *  get all MediaItem object from database to display
     * */
    LiveData<List<MediaItem>> getAllPlaylistMedias(){
        return mAllMedias;
    }

    /**
     *  get all MediaItem object with id of playlist order by desc from database to display
     * */
    public LiveData<List<MediaItem>> sortAllMediaByNameDESCWithID(int id){
        return mRepository.sortAllMediaByNameDESCWithID(id);
    }

    /**
     *  get all MediaItem object with id of playlist order by asc from database to display
     * */
    public LiveData<List<MediaItem>> sortAllMediaByNameASCWithID(int id){
        return mRepository.sortAllMediaByNameASCWithID(id);
    }

    /**
     *  insert MediaItem object to database
     * */
    public void insert(MediaItem media){
        mRepository.insert(media);
    }

    /**
     *  update MediaItem object from database
     * */
    public void update(MediaItem media){
        mRepository.update(media);
    }

    /**
     *  delete MediaItem object from database
     * */
    public void delete(MediaItem media){
        mRepository.delete(media);
    }

    /**
     *  delete All MediaItem object with id of playlist from database
     * */
    public void deleteAllWithID(int id){
        mRepository.deleteAllWithID(id);
    }

    /**
     *  get count of all MediaItem with id of a playlist
     * */
    public int getCountPlaylistWithID(int id) {
        return mRepository.getCountPlaylistWithID(id);
    }

    /**
     *  delete MediaItem object from database
     * */
    public void deleteAll(){
        mRepository.deleteAll();
    }

    /**
     *  delete MediaItem object from database with Uri
     * */
    public void deleteItemWithUri(String uri){
        mRepository.deleteItemWithUri(uri);
    }

}
