package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mediaplayerapp.ui.playlist.playlist_details.PlaylistMedia;
import com.example.mediaplayerapp.ui.playlist.playlist_details.PlaylistMediaRoomDatabase;

import java.util.List;

public class MediaQueueRepository {
    private MediaQueueDao mMediaQueueDao;
    public MediaQueueRepository(Application application) {
        MediaQueueRoomDatabase database = MediaQueueRoomDatabase.getDatabase(application);
        mMediaQueueDao = database.mediaQueueDao();
    }

    public void insert(MediaQueue media) {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMediaQueueDao.insert(media);
        });
    }

    public void delete(MediaQueue media) {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMediaQueueDao.delete(media);
        });
    }

    public void deleteAll() {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMediaQueueDao.deleteAll();
        });
    }
    public int getCountMediaQueue(){
        return mMediaQueueDao.getCountMediaQueue();
    }

    public LiveData<List<MediaQueue>> getAllMediaQueue() {
        return mMediaQueueDao.getAllMediaQueue();
    }
}
