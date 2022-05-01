package com.example.mediaplayerapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.net.URI;

@Entity(tableName = "playlist_table")
public class Playlist implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PlaylistID")
    private int id;

    @ColumnInfo(name = "ResourceID")
    private int idResource;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Numbers", defaultValue = "0")
    private int numbers;

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    @ColumnInfo(name = "MediaUri")
    private String mediaUri;

    public Playlist(int idResource, String name, int numbers, boolean isVideo, String mediaUri) {
        this.idResource = idResource;
        this.name = name;
        this.numbers = numbers;
        this.isVideo = isVideo;
        this.mediaUri = mediaUri;
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

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }
}
