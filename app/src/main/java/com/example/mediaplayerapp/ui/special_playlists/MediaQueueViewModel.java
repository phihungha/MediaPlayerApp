package com.example.mediaplayerapp.ui.special_playlists;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mediaplayerapp.data.special_playlists.MediaQueue;
import com.example.mediaplayerapp.data.special_playlists.MediaQueueRepository;
import com.example.mediaplayerapp.ui.special_playlists.PlaylistConstants;

import java.util.List;

public class MediaQueueViewModel extends AndroidViewModel {
    private final MediaQueueRepository mRepository;

    public MediaQueueViewModel(@NonNull Application application) {
        super(application);
        mRepository=new MediaQueueRepository(application);
    }

    /**
     *  insert MediaQueue object to database
     * */
    public void insert(MediaQueue media){
        mRepository.insert(media);
    }

    /**
     *  update MediaQueue object to database
     * */
    public void update(MediaQueue media){
        mRepository.update(media);
    }

    public void updateByList(List<MediaQueue> list) {
        mRepository.updateByList(list);
    }

    /**
     *  delete MediaQueue object from database
     * */
    public void delete(MediaQueue media){
        mRepository.delete(media);
    }

    /**
     *  delete All MediaQueue object from database
     * */
    public void deleteAll(){
        mRepository.deleteAll();
    }

    /**
     *  delete All MediaQueue object from database with Uri
     * */
    public void deleteItemWithUri(String uri){
        mRepository.deleteItemWithUri(uri);
    }

    /**
     *  get count of MediaQueue object from database
     * */
    public int getCountMediaQueue() {
        return mRepository.getCountMediaQueue();
    }

    /**
     *  get all MediaQueue object from database to display
     * */
    public LiveData<List<MediaQueue>> getAllMediaQueue() {
        return mRepository.getAllMediaQueue();
    }

    /**
     *  get all Video watch later item from database to display
     * */
    public LiveData<List<MediaQueue>> getAllVideoQueue() {
        return mRepository.getAllVideoQueue();
    }

    /**
     *  get all Music watch later item from database to display
     * */
    public LiveData<List<MediaQueue>> getAllMusicQueue() {
        return mRepository.getAllMusicQueue();
    }

    /**
     *  get all Favourite video item from database to display
     * */
    public LiveData<List<MediaQueue>> getAllVideoFavourite() {
        return mRepository.getAllVideoFavourite();
    }

    /**
     *  get all Favourite music item from database to display
     * */
    public LiveData<List<MediaQueue>> getAllMusicFavourite() {
        return mRepository.getAllMusicFavourite();
    }

    public List<MediaQueue> getCurrentListVideoWatchLater() {
        return mRepository.getCurrentList(PlaylistConstants.TYPE_VIDEO_QUEUE);
    }

    public List<MediaQueue> getCurrentListVideoFavourite() {
        return mRepository.getCurrentList(PlaylistConstants.TYPE_VIDEO_FAVOURITE);
    }

    public List<MediaQueue> getCurrentListSongWatchLater() {
        return mRepository.getCurrentList(PlaylistConstants.TYPE_MUSIC_QUEUE);
    }

    public List<MediaQueue> getCurrentListSongFavourite() {
        return mRepository.getCurrentList(PlaylistConstants.TYPE_MUSIC_FAVOURITE);
    }
}
