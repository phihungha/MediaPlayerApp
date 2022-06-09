package com.example.mediaplayerapp.data.playlist;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlaylistItems")
public class PlaylistItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PlaylistItemId")
    private int id;

    @ColumnInfo(name = "PlaylistId")
    private int playlistId;

    @NonNull
    @ColumnInfo(name = "MediaUri")
    private String mediaUri;

    @ColumnInfo(name = "OrderIndex")
    private int orderIndex;

    public PlaylistItem(int id, int playlistId, @NonNull String mediaUri, int orderIndex) {
        this.id = id;
        this.playlistId = playlistId;
        this.mediaUri = mediaUri;
        this.orderIndex = orderIndex;
    }

    @Ignore
    public PlaylistItem(int playlistId, @NonNull String mediaUri) {
        this.playlistId = playlistId;
        this.mediaUri = mediaUri;
        this.orderIndex = -1;
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

    public void setMediaUri(@NonNull String mediaUri) {
        this.mediaUri = mediaUri;
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
