package com.example.mediaplayerapp.data.playlist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Playlists")
public class Playlist {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PlaylistId")
    private int id;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    @ColumnInfo(name = "ItemCount")
    private int itemCount;

    public Playlist(String name, boolean isVideo, int itemCount) {
        this.name = name;
        this.isVideo = isVideo;
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
