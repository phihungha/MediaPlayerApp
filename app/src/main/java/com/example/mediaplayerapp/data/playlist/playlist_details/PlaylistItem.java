package com.example.mediaplayerapp.data.playlist.playlist_details;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "PlaylistItems",
        primaryKeys = {"PlaylistItemId", "MediaUri"})
public class PlaylistItem implements Serializable {
    @ColumnInfo(name = "PlaylistItemId")
    private int id;

    @NonNull
    @ColumnInfo(name = "MediaUri")
    private final String mediaUri;

    @ColumnInfo(name = "OrderSort")
    private long orderSort;

    public PlaylistItem(int id, @NonNull String mediaUri, long orderSort) {
        this.id = id;
        this.mediaUri = mediaUri;
        this.orderSort = orderSort;
    }

    public void setOrderSort(long orderSort) {
        this.orderSort = orderSort;
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

    public long getOrderSort() {
        return orderSort;
    }
}
