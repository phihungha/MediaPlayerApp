package com.example.mediaplayerapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_table")
public class Playlist {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PlaylistID")
    private int id;

    @ColumnInfo(name = "ResourceID")
    private int idResource;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Numbers")
    private int numbers;

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    @ColumnInfo(name = "SongID")
    private int songID;

    @ColumnInfo(name = "VideoID")
    private int videoID;

    public Playlist(int idResource, String name, int numbers, boolean isVideo, int songID, int videoID) {
        this.idResource = idResource;
        this.name = name;
        this.numbers = numbers;
        this.isVideo = isVideo;
        this.songID = songID;
        this.videoID = videoID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdResource() {
        return idResource;
    }

    public void setIdResource(int idResource) {
        this.idResource = idResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }
}
