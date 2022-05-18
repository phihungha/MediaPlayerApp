package com.example.mediaplayerapp.data.playlist.media_queue;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mediaQueue_table")
public class MediaQueue implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MediaQueueId")
    private int id;

    @ColumnInfo(name = "MediaQueueUri")
    private String mediaUri;

    @ColumnInfo(name = "MediaQueueName")
    private String name;

    public MediaQueue(String mediaUri, String name) {
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