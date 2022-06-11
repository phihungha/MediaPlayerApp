package com.example.mediaplayerapp.data.playback_history;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaybackHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(PlaybackHistoryEntry playbackHistoryEntry);

    @Update
    Completable update(PlaybackHistoryEntry playbackHistoryEntry);

    @Query("DELETE FROM PlaylistHistory")
    Completable deleteAll();

    @Query("SELECT * FROM PlaylistHistory " +
            "WHERE isVideo = 1 " +
            "ORDER BY lastPlaybackTime DESC " +
            "LIMIT :mediaShownCount")
    Flowable<List<PlaybackHistoryEntry>> getRecentVideos(int mediaShownCount);

    @Query("SELECT * FROM PlaylistHistory " +
            "WHERE IsVideo = 1 " +
            "ORDER BY playbackCount DESC " +
            "LIMIT :mediaShownCount")
    Flowable<List<PlaybackHistoryEntry>> getMostWatchedVideos(int mediaShownCount);

    @Query("SELECT * FROM PlaylistHistory " +
            "WHERE isVideo = 0 " +
            "ORDER BY lastPlaybackTime DESC " +
            "LIMIT :mediaShownCount")
    Flowable<List<PlaybackHistoryEntry>> getRecentSongs(int mediaShownCount);

    @Query("SELECT * FROM PlaylistHistory " +
            "WHERE IsVideo = 0 " +
            "ORDER BY playbackCount DESC " +
            "LIMIT :mediaShownCount")
    Flowable<List<PlaybackHistoryEntry>> getMostListenedSongs(int mediaShownCount);

    @Query("SELECT * FROM PlaylistHistory " +
            "WHERE MediaURI = :mediaUri")
    Single<List<PlaybackHistoryEntry>> getByMediaUri(String mediaUri);
}
