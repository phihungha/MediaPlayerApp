package com.example.mediaplayerapp.data.playlist;

import android.app.Application;

import com.example.mediaplayerapp.data.AppRoomDatabase;

import java.util.List;
import java.util.stream.IntStream;

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
        int playlistId = item.getPlaylistId();
        return playlistRepository.getPlaylistItemCount(playlistId)
                        .flatMapCompletable(i -> {
                            item.setOrderIndex(i);
                            return playlistItemDao.insert(item);
                        })
                        .andThen(updatePlaylistItemCount(playlistId))
                        .subscribeOn(Schedulers.io());
    }

    public Completable addPlaylistItems(List<PlaylistItem> items) {
        int playlistId = items.get(0).getPlaylistId();
        return playlistRepository.getPlaylistItemCount(playlistId)
                .flatMapCompletable(count -> {
                    IntStream.range(0, items.size())
                             .forEach(i -> items.get(i).setOrderIndex(count + i));
                    return playlistItemDao.insert(items);
                })
                .andThen(updatePlaylistItemCount(playlistId))
                .subscribeOn(Schedulers.io());
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

    public Completable deletePlaylistItem(PlaylistItem item) {
        return playlistItemDao.delete(item).subscribeOn(Schedulers.io());
    }

    public Flowable<List<PlaylistItem>> getAllItemsOfPlaylist(int playlistId) {
        return playlistItemDao.getAllOfPlaylist(playlistId)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<PlaylistItem> getFirstItemOfPlaylist(int playlistId) {
        return playlistItemDao.getFirstItemOfPlaylist(playlistId).subscribeOn(Schedulers.io());
    }
}
