package com.example.mediaplayerapp.data.playlist;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PlaylistRepository {
    private final PlaylistDao mPlaylistDao;
    private final LiveData<List<Playlist>> mAllPlaylists;

    public PlaylistRepository(Application application) {
        PlaylistRoomDatabase database = PlaylistRoomDatabase.getDatabase(application);
        mPlaylistDao = database.playlistDao();
        mAllPlaylists = mPlaylistDao.getAllPlaylists();
    }

    public void insert(Playlist playlist) {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistDao.insert(playlist));
    }

    public void delete(Playlist playlist) {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistDao.delete(playlist));
    }

    public void update(Playlist playlist) {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> mPlaylistDao.update(playlist));
    }

    public void deleteAll() {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(mPlaylistDao::deleteAll);
    }

    public LiveData<List<Playlist>> getPlaylistWithID(int id){
        return mPlaylistDao.getPlayListWithID(id);
    }

    public LiveData<List<Playlist>> sortPlaylistByNameASC(){
        return mPlaylistDao.sortPlaylistByNameASC();
    }

    public LiveData<List<Playlist>> sortPlaylistByNameDESC(){
        return mPlaylistDao.sortPlaylistByNameDESC();
    }

    public LiveData<List<Playlist>> sortPlaylistByNumberItemASC(){
        return mPlaylistDao.sortPlaylistByNumberItemASC();
    }

    public LiveData<List<Playlist>> sortPlaylistByNumberItemDESC(){
        return mPlaylistDao.sortPlaylistByNumberItemDESC();
    }

    public LiveData<List<Playlist>> getAllPlaylistSearching(String text) {
        return mPlaylistDao.getAllPlaylistSearching(text);
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return mAllPlaylists;
    }
}
