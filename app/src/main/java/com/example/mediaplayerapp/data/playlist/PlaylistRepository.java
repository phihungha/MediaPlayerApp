package com.example.mediaplayerapp.data.playlist;

import android.app.Application;

import com.example.mediaplayerapp.data.AppRoomDatabase;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.flow.Flow;

public class PlaylistRepository {

    public enum SortBy {
        NAME,
        ITEM_COUNT
    }

    private final PlaylistDao playlistDao;
    private final PlaylistItemDao playlistItemDao;

    public PlaylistRepository(Application application) {
        playlistDao = AppRoomDatabase.getDatabase(application).playlistDao();
        playlistItemDao = AppRoomDatabase.getDatabase(application).playlistItemDao();
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

    public Completable updatePlaylistItemCount(int playlistId, int newCount) {
        return playlistDao.updateItemCount(playlistId, newCount).subscribeOn(Schedulers.io());
    }

    public Completable deletePlaylist(int playlistId) {
        return playlistItemDao.deleteAllFromPlaylist(playlistId)
                .andThen(playlistDao.delete(playlistId))
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Playlist> getPlaylist(int playlistId){
        return playlistDao.get(playlistId).subscribeOn(Schedulers.io());
    }

    public Single<Integer> getPlaylistItemCount(int playlistId){
        return playlistItemDao.getItemCountOfPlaylist(playlistId)
                .subscribeOn(Schedulers.io());
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

    public Flowable<List<Playlist>> getAllSongPlaylists() {
        return playlistDao.getAllSongPlaylists().subscribeOn(Schedulers.io());
    }

    public Flowable<List<Playlist>> getAllVideoPlaylists() {
        return playlistDao.getAllVideoPlaylists().subscribeOn(Schedulers.io());
    }
}
