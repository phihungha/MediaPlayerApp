package com.example.mediaplayerapp.data.playlist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "playlist_table")
public class Playlist implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PlaylistID")
    private int id;

    @ColumnInfo(name = "ResourceID")
    private int idResource;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    public Playlist(int idResource, String name, boolean isVideo) {
        this.idResource = idResource;
        this.name = name;
        this.isVideo = isVideo;
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

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
