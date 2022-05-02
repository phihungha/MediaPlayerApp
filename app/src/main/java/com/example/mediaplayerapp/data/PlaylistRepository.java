package com.example.mediaplayerapp.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PlaylistRepository {
    private PlaylistDao mPlaylistDao;
    private LiveData<List<Playlist>> mAllPlaylists;

    public PlaylistRepository(Application application) {
        PlaylistRoomDatabase database = PlaylistRoomDatabase.getDatabase(application);
        mPlaylistDao = database.playlistDao();
        mAllPlaylists = mPlaylistDao.getAllPlaylists();
    }

    public void insert(Playlist playlist) {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistDao.insert(playlist);
        });
    }

    public void delete(Playlist playlist) {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistDao.delete(playlist);
        });
    }

    public void update(Playlist playlist) {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistDao.update(playlist);
        });
    }

    public void deleteAll() {
        PlaylistRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPlaylistDao.deleteAll();
        });
    }

/*    public LiveData<List<Playlist>> getPlaylistWithID(int id){
        return mPlaylistDao.getPlayListWithID(id);
    }*/

    public LiveData<List<Playlist>> getAllPlaylists() {
        return mAllPlaylists;
    }
}
