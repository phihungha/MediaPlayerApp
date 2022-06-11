package com.example.mediaplayerapp.data.playback_history;

import android.app.Application;
import android.net.Uri;

import com.example.mediaplayerapp.data.AppRoomDatabase;

import java.time.Instant;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlaybackHistoryRepository {
    private final PlaybackHistoryDao playbackHistoryDao;

    public PlaybackHistoryRepository(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        playbackHistoryDao = database.playbackHistoryDao();
    }

    public Completable insert(PlaybackHistoryEntry playbackHistoryEntry) {
        return playbackHistoryDao.insert(playbackHistoryEntry)
                .subscribeOn(Schedulers.io());
    }

    public Completable update(PlaybackHistoryEntry playbackHistoryEntry) {
        return playbackHistoryDao.update(playbackHistoryEntry)
                .subscribeOn(Schedulers.io());
    }

    public Completable clearHistory() {
        return playbackHistoryDao.deleteAll().subscribeOn(Schedulers.io());
    }

    public Single<List<PlaybackHistoryEntry>> getByMediaUri(Uri mediaUri) {
        return playbackHistoryDao.getByMediaUri(mediaUri.toString())
                .subscribeOn(Schedulers.io());
    }

    private Completable recordHistory(Uri mediaUri, boolean isVideo, long lastPlaybackPosition) {
        return getByMediaUri(mediaUri)
                .flatMapCompletable(playbackHistoryEntries -> {

                    long lastPlaybackTime = Instant.now().toEpochMilli();
                    if (playbackHistoryEntries.size() == 1) {
                        PlaybackHistoryEntry entry = playbackHistoryEntries.get(0);
                        entry.incrementPlaybackCount();
                        entry.setLastPlaybackPosition(lastPlaybackPosition);
                        entry.setLastPlaybackTime(lastPlaybackTime);
                        return update(entry);
                    }

                    PlaybackHistoryEntry entry
                            = new PlaybackHistoryEntry(mediaUri.toString(),
                            lastPlaybackTime,
                            1,
                            isVideo,
                            lastPlaybackPosition);
                    return insert(entry);

                    }).subscribeOn(Schedulers.io());
    }

    public Completable recordMusicHistory(Uri mediaUri, long lastPlaybackPosition) {
        return recordHistory(mediaUri, false, lastPlaybackPosition);
    }

    public Completable recordVideoHistory(Uri mediaUri, long lastPlaybackPosition) {
        return recordHistory(mediaUri, true, lastPlaybackPosition);
    }

    public Flowable<List<PlaybackHistoryEntry>> getRecentVideos(int mediaShownCount) {
        return playbackHistoryDao.getRecentVideos(mediaShownCount)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<PlaybackHistoryEntry>> getMostWatchedVideos(int mediaShownCount) {
        return playbackHistoryDao.getMostWatchedVideos(mediaShownCount)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<PlaybackHistoryEntry>> getRecentSongs(int mediaShownCount) {
        return playbackHistoryDao.getRecentSongs(mediaShownCount)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<PlaybackHistoryEntry>> getMostListenedSongs(int mediaShownCount) {
        return playbackHistoryDao.getMostListenedSongs(mediaShownCount)
                .subscribeOn(Schedulers.io());
    }
}
