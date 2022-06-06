package com.example.mediaplayerapp.data.playlist.media_queue;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;

import java.util.List;

public class MediaQueueRepository {
    private final MediaQueueDao mMediaQueueDao;
    public MediaQueueRepository(Application application) {
        MediaQueueRoomDatabase database = MediaQueueRoomDatabase.getDatabase(application);
        mMediaQueueDao = database.mediaQueueDao();
    }

    public void insert(MediaQueue media) {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> mMediaQueueDao.insert(media));
    }

    public void update(MediaQueue media) {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> mMediaQueueDao.update(media));
    }

    public void updateByList(List<MediaQueue> list) {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> mMediaQueueDao.updateByList(list));
    }

    public void delete(MediaQueue media) {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> mMediaQueueDao.delete(media));
    }

    public void deleteAll() {
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(mMediaQueueDao::deleteAll);
    }

    public void deleteItemWithUri(String uri){
        MediaQueueRoomDatabase.databaseWriteExecutor.execute(() -> mMediaQueueDao.deleteItemWithUri(uri));
    }

    public int getCountMediaQueue(){
        return mMediaQueueDao.getCountMediaQueue();
    }

    public List<MediaQueue> getCurrentList(int type) {
        return mMediaQueueDao.getCurrentList(type);
    }

    public LiveData<List<MediaQueue>> getAllVideoQueue() {
        return mMediaQueueDao.getAllQueueWithType(PlaylistConstants.TYPE_VIDEO_QUEUE);
    }

    public LiveData<List<MediaQueue>> getAllMusicQueue() {
        return mMediaQueueDao.getAllQueueWithType(PlaylistConstants.TYPE_MUSIC_QUEUE);
    }

    public LiveData<List<MediaQueue>> getAllVideoFavourite() {
        return mMediaQueueDao.getAllQueueWithType(PlaylistConstants.TYPE_VIDEO_FAVOURITE);
    }

    public LiveData<List<MediaQueue>> getAllMusicFavourite() {
        return mMediaQueueDao.getAllQueueWithType(PlaylistConstants.TYPE_MUSIC_FAVOURITE);
    }

    public LiveData<List<MediaQueue>> getAllMediaQueue() {
        return mMediaQueueDao.getAllMediaQueue();
    }
}
