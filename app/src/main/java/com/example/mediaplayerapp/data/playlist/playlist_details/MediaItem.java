package com.example.mediaplayerapp.data.playlist.playlist_details;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

/***
 *  Media class for playlist
 */
@Entity(tableName = "media_table",
        primaryKeys = {"MediaId","MediaUri"})
public class MediaItem implements Serializable {
    @ColumnInfo(name = "MediaId")
    private int id;

    @NonNull
    @ColumnInfo(name = "MediaUri")
    private String mediaUri;

    @ColumnInfo(name = "MediaName")
    private String name;

    public MediaItem(int id, @NonNull String mediaUri, String name) {
        this.id = id;
        this.mediaUri = mediaUri;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
