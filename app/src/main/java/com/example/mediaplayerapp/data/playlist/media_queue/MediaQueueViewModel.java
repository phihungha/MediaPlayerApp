package com.example.mediaplayerapp.data.playlist.media_queue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueRepository;

import java.util.List;

public class MediaQueueViewModel extends AndroidViewModel {
    private MediaQueueRepository mRepository;

    public MediaQueueViewModel(@NonNull Application application) {
        super(application);
        mRepository=new MediaQueueRepository(application);
    }

    public void insert(MediaQueue media){
        mRepository.insert(media);
    }

    public void delete(MediaQueue media){
        mRepository.delete(media);
    }

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public int getCountMediaQueue() {
        return mRepository.getCountMediaQueue();
    }

    public LiveData<List<MediaQueue>> getAllMediaQueue() {
        return mRepository.getAllMediaQueue();
    }
}
