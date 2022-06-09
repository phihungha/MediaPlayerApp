package com.example.mediaplayerapp.data.playlist;

import android.app.Application;

import com.example.mediaplayerapp.data.AppRoomDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaylistItemRepository {
    private final PlaylistItemDao playlistItemDao;
    private final PlaylistRepository playlistRepository;

    public PlaylistItemRepository(Application application) {
        playlistItemDao = AppRoomDatabase.getDatabase(application).playlistItemDao();
        playlistRepository = new PlaylistRepository(application);
    }

    public Completable addPlaylistItem(PlaylistItem item) {
        item.setPlaylistId(item.getPlaylistId());
        return playlistItemDao.insert(item)
                .andThen(updatePlaylistItemCount(item.getPlaylistId()))
                .subscribeOn(Schedulers.io());
    }

    public Completable addPlaylistItems(List<PlaylistItem> items) {
        return playlistItemDao.insertMany(items)
                .andThen(updatePlaylistItemCount(items.get(0).getPlaylistId()))
                .subscribeOn(Schedulers.io());
    }

    public Completable updatePlaylistItem(PlaylistItem item) {
        return playlistItemDao.update(item).subscribeOn(Schedulers.io());
    }

    public Completable updatePlaylistItems(List<PlaylistItem> items) {
        return playlistItemDao.update(items).subscribeOn(Schedulers.io());
    }

    private Completable updatePlaylistItemCount(int playlistId) {
        return playlistItemDao.getItemCountOfPlaylist(playlistId)
                .flatMapCompletable(i -> playlistRepository.updatePlaylistItemCount(playlistId, i))
                .subscribeOn(Schedulers.io());
    }

    public Completable deletePlaylistItem(int itemId) {
        return playlistItemDao.delete(itemId).subscribeOn(Schedulers.io());
    }

    public Flowable<List<PlaylistItem>> getAllItemsOfPlaylist(int playlistId) {
        return playlistItemDao.getAllOfPlaylist(playlistId)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<PlaylistItem> getFirstItemOfPlaylist(int playlistId) {
        return playlistItemDao.getFirstItemOfPlaylist(playlistId).subscribeOn(Schedulers.io());
    }

    public Completable deleteAllFromPlaylist(int playlistId) {
        return playlistItemDao.deleteAllFromPlaylist(playlistId).subscribeOn(Schedulers.io());
    }
}
