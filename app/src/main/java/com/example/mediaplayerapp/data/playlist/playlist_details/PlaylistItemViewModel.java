package com.example.mediaplayerapp.data.playlist.playlist_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaylistItemViewModel extends AndroidViewModel{
    private final PlaylistItemRepository mRepository;
    private final LiveData<List<PlaylistItem>> mAllMedias;

    public PlaylistItemViewModel(@NonNull Application application) {
        super(application);
        mRepository=new PlaylistItemRepository(application);
        mAllMedias =mRepository.getAllPlaylistMedias();
    }

    /**
     *  get all MediaItem object with id of playlist from database to display
     * */
    public LiveData<List<PlaylistItem>> getAllPlaylistMediasWithID(int id){
        return mRepository.getAllPlaylistMediasWithID(id);
    }

    /**
     *  get all MediaItem object from database to display
     * */
    LiveData<List<PlaylistItem>> getAllPlaylistMedias(){
        return mAllMedias;
    }

    /**
     *  insert MediaItem object to database
     * */
    public void insert(PlaylistItem media){
        mRepository.insert(media);
    }

    /**
     *  update MediaItem object from database
     * */
    public void update(PlaylistItem media){
        mRepository.update(media);
    }

    /**
     *  update multiple item from database
     * */
    public void updateMultiple(PlaylistItem... playlistItems){
        mRepository.updateMultiple(playlistItems);
    }

    /**
     *  update multiple item by list from database
     * */
    public void updateByList(List<PlaylistItem> list){
        mRepository.updateByList(list);
    }

    /**
     *  delete MediaItem object from database
     * */
    public void delete(PlaylistItem media){
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
     *  get first item with id
     * */
    public PlaylistItem findByItemId(int id) {
        return mRepository.findByItemId(id);
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

    /**
     *  get Current list
     * */
    public List<PlaylistItem> getCurrentListWithID(int id) {
        return mRepository.getCurrentListWithID(id);
    }
}
