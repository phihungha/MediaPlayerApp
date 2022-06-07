package com.example.mediaplayerapp.data.playlist.playlist_details;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

/***
 *  Media class for playlist
 */
@Entity(tableName = "media_table",
        primaryKeys = {"MediaId", "MediaUri"}
       )
public class PlaylistItem implements Serializable {
    @ColumnInfo(name = "MediaId")
    private int id;

    @NonNull
    @ColumnInfo(name = "MediaUri")
    private String mediaUri;

    @ColumnInfo(name = "OrderSort")
    private long orderSort;

    public PlaylistItem(int id, @NonNull String mediaUri, long orderSort) {
        this.id = id;
        this.mediaUri = mediaUri;
        this.orderSort = orderSort;
    }

    public long getOrderSort() {
        return orderSort;
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

    public void setMediaUri(@NonNull String mediaUri) {
        this.mediaUri = mediaUri;
    }

}
