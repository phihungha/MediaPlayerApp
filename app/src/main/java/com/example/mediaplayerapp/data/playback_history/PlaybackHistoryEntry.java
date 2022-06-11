package com.example.mediaplayerapp.data.playback_history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlaylistHistory")
public class PlaybackHistoryEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PlaybackHistoryEntryId")
    private int id;

    @ColumnInfo(name = "MediaURI")
    private String mediaUri;

    @ColumnInfo(name = "LastPlaybackTime")
    private long lastPlaybackTime;

    @ColumnInfo(name = "LastPlaybackPosition")
    private long lastPlaybackPosition;

    @ColumnInfo(name = "PlaybackCount")
    private int playbackCount;

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    public PlaybackHistoryEntry(String mediaUri, long lastPlaybackTime, int playbackCount, boolean isVideo, long lastPlaybackPosition) {
        this.setLastPlaybackPosition(lastPlaybackPosition);
        this.setMediaUri(mediaUri);
        this.setLastPlaybackTime(lastPlaybackTime);
        this.setPlaybackCount(playbackCount);
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

    public int getPlaybackCount() {
        return playbackCount;
    }

    public void setPlaybackCount(int playbackCount) {
        this.playbackCount = playbackCount;
    }

    public void incrementPlaybackCount() {
        playbackCount++;
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

    public long getLastPlaybackPosition() {
        return lastPlaybackPosition;
    }

    public void setLastPlaybackPosition(long lastPlaybackPosition) {
        this.lastPlaybackPosition = lastPlaybackPosition;
    }
}
