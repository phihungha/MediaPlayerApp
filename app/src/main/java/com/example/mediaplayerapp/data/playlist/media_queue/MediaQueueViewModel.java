package com.example.mediaplayerapp.data.playlist.media_queue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
}
