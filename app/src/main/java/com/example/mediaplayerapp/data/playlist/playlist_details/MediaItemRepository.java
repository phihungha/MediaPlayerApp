package com.example.mediaplayerapp.data.playlist.playlist_details;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MediaItemRepository {
    private final MediaItemDao mMediaItemDao;
    private final LiveData<List<MediaItem>> mAllMedias;

    public MediaItemRepository(Application application) {
        MediaItemRoomDatabase database = MediaItemRoomDatabase.getDatabase(application);
        mMediaItemDao = database.playlistMediaDao();
        mAllMedias= mMediaItemDao.getAllPlaylistMedias();
    }

    public void insert(MediaItem media) {
        MediaItemRoomDatabase.databaseWriteExecutor.execute(() -> mMediaItemDao.insert(media));
    }

    public void delete(MediaItem media) {
        MediaItemRoomDatabase.databaseWriteExecutor.execute(() -> mMediaItemDao.delete(media));
    }

    public void update(MediaItem media) {
        MediaItemRoomDatabase.databaseWriteExecutor.execute(() -> mMediaItemDao.update(media));
    }

    public void deleteAll() {
        MediaItemRoomDatabase.databaseWriteExecutor.execute(mMediaItemDao::deleteAll);
    }

    public void deleteAllWithID(int id){
        MediaItemRoomDatabase.databaseWriteExecutor.execute(() -> mMediaItemDao.deleteAllWithID(id));
    }

    public void deleteItemWithUri(String uri){
        MediaItemRoomDatabase.databaseWriteExecutor.execute(() -> mMediaItemDao.deleteItemWithUri(uri));
    }

    public int getCountPlaylistWithID(int id) {
        return mMediaItemDao.getCountPlaylistWithID(id);
    }

    public LiveData<List<MediaItem>> getAllPlaylistMediasWithID(int id) {
        return mMediaItemDao.getAllPlaylistMediasWithID(id);
    }

    public LiveData<List<MediaItem>> getAllMediaSearching(String text){
        return mMediaItemDao.getAllMediaSearching(text);
    }

    public LiveData<List<MediaItem>> sortAllMediaByNameASCWithID(int id){
        return mMediaItemDao.sortAllMediaByNameASCWithID(id);
    }

    public LiveData<List<MediaItem>> sortAllMediaByNameDESCWithID(int id){
        return mMediaItemDao.sortAllMediaByNameDESCWithID(id);
    }

    public LiveData<List<MediaItem>> getAllPlaylistMedias() {
        return mAllMedias;
    }
}
