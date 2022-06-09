package com.example.mediaplayerapp.data.playlist;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "PlaylistItems", primaryKeys = {"PlaylistItemId"})
public class PlaylistItem implements Serializable {

    @ColumnInfo(name = "PlaylistItemId")
    private int id;

    @ColumnInfo(name = "PlaylistId")
    private int playlistId;

    @NonNull
    @ColumnInfo(name = "MediaUri")
    private final String mediaUri;

    @ColumnInfo(name = "OrderIndex")
    private int orderIndex;

    public PlaylistItem(int id, int playlistId, @NonNull String mediaUri, int orderIndex) {
        this.id = id;
        this.playlistId = playlistId;
        this.mediaUri = mediaUri;
        this.orderIndex = orderIndex;
    }

    public PlaylistItem(int playlistId, @NonNull String mediaUri) {
        this.playlistId = playlistId;
        this.mediaUri = mediaUri;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getMediaUri() {
        return mediaUri;
    }

    public Uri getAndroidMediaUri() {
        return Uri.parse(getMediaUri());
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}
