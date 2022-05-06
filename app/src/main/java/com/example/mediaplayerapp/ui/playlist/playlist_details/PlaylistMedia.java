package com.example.mediaplayerapp.ui.playlist.playlist_details;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/***
 *  Media class for playlist
 */
@Entity(tableName = "media_table",
        primaryKeys = {"MediaId","MediaUri"})
public class PlaylistMedia {
    @NonNull
    @ColumnInfo(name = "MediaId")
    private int id;

    @NonNull
    @ColumnInfo(name = "MediaUri")
    private String mediaUri;

    @ColumnInfo(name = "MediaName")
    private String name;

    public PlaylistMedia(int id, String mediaUri, String name) {
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

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
