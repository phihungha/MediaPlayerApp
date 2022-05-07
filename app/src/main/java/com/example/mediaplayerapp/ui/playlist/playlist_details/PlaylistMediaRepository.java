package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaylistMediaRepository {
    private PlaylistMediaDao mPlaylistMediaDao;
    private LiveData<List<PlaylistMedia>> mAllMedias;

    public PlaylistMediaRepository(Application application) {
        PlaylistMediaRoomDatabase database = PlaylistMediaRoomDatabase.getDatabase(application);
        mPlaylistMediaDao = database.playlistMediaDao();
        mAllMedias=mPlaylistMediaDao.getAllPlaylistMedias();
    }

    public void insert(PlaylistMedia media) {
        PlaylistMediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistMediaDao.insert(media);
        });
    }

    public void delete(PlaylistMedia media) {
        PlaylistMediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistMediaDao.delete(media);
        });
    }

    public void update(PlaylistMedia media) {
        PlaylistMediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistMediaDao.update(media);
        });
    }

    public void deleteAll() {
        PlaylistMediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistMediaDao.deleteAll();
        });
    }

    public void deleteAllWithID(int id){
        PlaylistMediaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistMediaDao.deleteAllWithID(id);
        });
    }

    public int getCountPlaylistWithID(int id) {
        return mPlaylistMediaDao.getCountPlaylistWithID(id);
    }

    public LiveData<List<PlaylistMedia>> getAllPlaylistMediasWithID(int id) {
        return mPlaylistMediaDao.getAllPlaylistMediasWithID(id);
    }

    public LiveData<List<PlaylistMedia>> getAllPlaylistMedias() {
        return mAllMedias;
    }
}
