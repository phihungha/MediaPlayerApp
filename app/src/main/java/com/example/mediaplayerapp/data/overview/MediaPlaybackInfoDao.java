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
            "SET playbackAmount = playbackAmount + 1 " +
            "WHERE MediaPlaybackInfoID = :MediaPlaybackInfoID")
    void updatePlaybackAmount(int MediaPlaybackInfoID);


    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE isVideo = 1 " +
            "ORDER BY lastPlaybackTime DESC " +
            "LIMIT 5")
    LiveData<List<MediaPlaybackInfo>> get5RecentVideos();

    @Query("SELECT * FROM media_playback_info_table " +
            "WHERE MediaURI = :mediaUri")
    MediaPlaybackInfo getMediaPlayBackInfoByUri(String mediaUri);

    @Query("SELECT * FROM media_playback_info_table")
    LiveData<List<MediaPlaybackInfo>> getAllMediaPlaybackInfo();
}
