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

    public Completable insert(Playlist playlist) {
        return playlistDao.insert(playlist).subscribeOn(Schedulers.io());
    }

    public Completable update(Playlist playlist) {
        return playlistDao.update(playlist).subscribeOn(Schedulers.io());
    }

    public Completable updateName(int playlistId, String newName) {
        return playlistDao.updateName(playlistId, newName).subscribeOn(Schedulers.io());
    }

    public Completable delete(Playlist playlist) {
        return playlistDao.delete(playlist).subscribeOn(Schedulers.io());
    }

    public Completable delete(int id) {
        return playlistDao.delete(id).subscribeOn(Schedulers.io());
    }

    public Flowable<List<Playlist>> getPlaylist(int id){
        return playlistDao.getPlaylist(id);
    }

    public Flowable<List<Playlist>> getPlaylistsByNameMatching(String text) {
        return playlistDao.getPlaylistsByNameMatching(text).subscribeOn(Schedulers.io());
    }

    public Flowable<List<Playlist>> getAllPlaylists(SortBy sortBy, SortOrder sortOrder) {
        if (sortBy == SortBy.NAME && sortOrder == SortOrder.ASC)
            return playlistDao.getAllPlaylistsSortedByNameAsc().subscribeOn(Schedulers.io());
        if (sortBy == SortBy.NAME && sortOrder == SortOrder.DESC)
            return playlistDao.getAllPlaylistsSortedByNameDesc().subscribeOn(Schedulers.io());
        if (sortBy == SortBy.ITEM_COUNT && sortOrder == SortOrder.ASC)
            return playlistDao.getAllPlaylistsSortedByItemCountAsc().subscribeOn(Schedulers.io());
        return playlistDao.getAllPlaylistsSortedByItemCountDesc().subscribeOn(Schedulers.io());
    }
}
