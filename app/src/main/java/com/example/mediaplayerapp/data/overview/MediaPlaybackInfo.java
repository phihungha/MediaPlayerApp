package com.example.mediaplayerapp.data.overview;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_playback_info_table")
public class MediaPlaybackInfo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MediaPlaybackInfoID")
    private int id;

    @ColumnInfo(name = "MediaURI")
    private String mediaUri;

    @ColumnInfo(name = "LastPlaybackTime")
    private long lastPlaybackTime;

    @ColumnInfo(name = "PlaybackAmount")
    private int playbackAmount;

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    public MediaPlaybackInfo(String mediaUri, long lastPlaybackTime, int playbackAmount, boolean isVideo) {
        this.setMediaUri(mediaUri);
        this.setLastPlaybackTime(lastPlaybackTime);
        this.setPlaybackAmount(playbackAmount);
        this.setVideo(isVideo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public int getPlaybackAmount() {
        return playbackAmount;
    }

    public void setPlaybackAmount(int playbackAmount) {
        this.playbackAmount = playbackAmount;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getLastPlaybackTime() {
        return lastPlaybackTime;
    }

    public void setLastPlaybackTime(long lastPlaybackTime) {
        this.lastPlaybackTime = lastPlaybackTime;
    }
}
