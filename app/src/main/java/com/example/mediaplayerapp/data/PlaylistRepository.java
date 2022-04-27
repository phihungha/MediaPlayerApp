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

    public LiveData<List<Playlist>> getAllPlaylists() {
        return mAllPlaylists;
    }
}
