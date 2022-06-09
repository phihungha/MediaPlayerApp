package com.example.mediaplayerapp.data.playlist;

import android.app.Application;

import com.example.mediaplayerapp.data.AppRoomDatabase;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaylistRepository {

    public enum SortBy {
        NAME,
        ITEM_COUNT
    }

    private final PlaylistDao playlistDao;

    public PlaylistRepository(Application application) {
        playlistDao = AppRoomDatabase.getDatabase(application).playlistDao();
    }

    public Completable addPlaylist(Playlist playlist) {
        return playlistDao.insert(playlist).subscribeOn(Schedulers.io());
    }

    public Completable updatePlaylist(Playlist playlist) {
        return playlistDao.update(playlist).subscribeOn(Schedulers.io());
    }

    public Completable renamePlaylist(int playlistId, String newName) {
        return playlistDao.updateName(playlistId, newName).subscribeOn(Schedulers.io());
    }

    public Completable deletePlaylist(Playlist playlist) {
        return playlistDao.delete(playlist).subscribeOn(Schedulers.io());
    }

    public Completable deletePlaylist(int id) {
        return playlistDao.delete(id).subscribeOn(Schedulers.io());
    }

    public Flowable<List<Playlist>> getPlaylist(int id){
        return playlistDao.get(id);
    }

    public Flowable<List<Playlist>> getPlaylistsByNameMatching(String text) {
        return playlistDao.getByNameMatching(text).subscribeOn(Schedulers.io());
    }

    public Flowable<List<Playlist>> getAllPlaylists(SortBy sortBy, SortOrder sortOrder) {
        if (sortBy == SortBy.NAME && sortOrder == SortOrder.ASC)
            return playlistDao.getAllSortedByNameAsc().subscribeOn(Schedulers.io());
        if (sortBy == SortBy.NAME && sortOrder == SortOrder.DESC)
            return playlistDao.getAllSortedByNameDesc().subscribeOn(Schedulers.io());
        if (sortBy == SortBy.ITEM_COUNT && sortOrder == SortOrder.ASC)
            return playlistDao.getAllSortedByItemCountAsc().subscribeOn(Schedulers.io());
        return playlistDao.getAllSortedByItemCountDesc().subscribeOn(Schedulers.io());
    }
}
