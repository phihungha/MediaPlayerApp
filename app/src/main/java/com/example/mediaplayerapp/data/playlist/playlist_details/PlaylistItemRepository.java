package com.example.mediaplayerapp.data.playlist.playlist_details;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlaylistItemRepository {
    private final PlaylistItemDao mPlaylistItemDao;
    private final LiveData<List<PlaylistItem>> mAllMedias;

    public PlaylistItemRepository(Application application) {
        PlaylistItemRoomDatabase database = PlaylistItemRoomDatabase.getDatabase(application);
        mPlaylistItemDao = database.playlistMediaDao();
        mAllMedias= mPlaylistItemDao.getAllPlaylistMedias();
    }

    public void insert(PlaylistItem media) {
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.insert(media));
    }

    public void delete(PlaylistItem media) {
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.delete(media));
    }

    public void update(PlaylistItem media) {
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.update(media));
    }

    public void updateMultiple(PlaylistItem... playlistItems) {
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.updateMultiple(playlistItems));
    }

    public void updateByList(List<PlaylistItem> list) {
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.updateByList(list));
    }

    public void deleteAll() {
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(mPlaylistItemDao::deleteAll);
    }

    public void deleteAllWithID(int id){
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.deleteAllWithID(id));
    }

    public void deleteItemWithUri(String uri){
        PlaylistItemRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistItemDao.deleteItemWithUri(uri));
    }

    public int getCountPlaylistWithID(int id) {
        return mPlaylistItemDao.getCountPlaylistWithID(id);
    }

    public PlaylistItem findByItemId(int id) {
        return mPlaylistItemDao.findByItemId(id);
    }

    public LiveData<List<PlaylistItem>> getAllPlaylistMediasWithID(int id) {
        return mPlaylistItemDao.getAllPlaylistMediasWithID(id);
    }


    public LiveData<List<PlaylistItem>> getAllPlaylistMedias() {
        return mAllMedias;
    }

    public List<PlaylistItem> getCurrentList() {
        return mPlaylistItemDao.getCurrentList();
    }
}
