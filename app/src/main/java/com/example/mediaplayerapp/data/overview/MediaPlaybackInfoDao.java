package com.example.mediaplayerapp.data.overview;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MediaPlaybackInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaPlaybackInfo mediaPlaybackInfo);

    @Update
    void update(MediaPlaybackInfo mediaPlaybackInfo);


    @Query("UPDATE media_playback_info_table " +
            "SET playbackCount = playbackCount + 1 " +
            "WHERE MediaPlaybackInfoID = :MediaPlaybackInfoID")
    void updatePlaybackCount(int MediaPlaybackInfoID);

    @Query("UPDATE media_playback_info_table SET " +
            "playbackCount = playbackCount + 1, " +
            "LastPlaybackTime = :lastPlaybackTime, " +
            "LastPlaybackPosition = :lastPlaybackPosition " +
            "WHERE MediaURI = :mediaUri")
    void updateRelevantInfo(String mediaUri, long lastPlaybackTime, long lastPlaybackPosition);

    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE isVideo = 1 " +
            "ORDER BY lastPlaybackTime DESC " +
            "LIMIT :mediaShownCount")
    LiveData<List<MediaPlaybackInfo>> getRecentVideos(int mediaShownCount);

    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE IsVideo = 1 " +
            "ORDER BY playbackCount DESC " +
            "LIMIT :mediaShownCount")
    LiveData<List<MediaPlaybackInfo>> getMostWatchedVideos(int mediaShownCount);

    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE isVideo = 0 " +
            "ORDER BY lastPlaybackTime DESC " +
            "LIMIT :mediaShownCount")
    LiveData<List<MediaPlaybackInfo>> getRecentSongs(int mediaShownCount);

    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE IsVideo = 0 " +
            "ORDER BY playbackCount DESC " +
            "LIMIT :mediaShownCount")
    LiveData<List<MediaPlaybackInfo>> getMostListenedSongs(int mediaShownCount);

    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE MediaURI = :mediaUri")
    MediaPlaybackInfo getMediaPlayBackInfoByUri(String mediaUri);

    @Query("SELECT * FROM media_playback_info_table")
    LiveData<List<MediaPlaybackInfo>> getAllMediaPlaybackInfo();
}
