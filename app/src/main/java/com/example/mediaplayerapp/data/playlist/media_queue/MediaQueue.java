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

    @ColumnInfo(name = "IsVideo")
    private boolean isVideo;

    /**
     * type = 1: Video queue
     * type = 2: Music queue
     * type = 3: Video favourite
     * type = 4: Music favourite
     * */
    @ColumnInfo(name = "Type")
    private int type;

    @ColumnInfo(name = "OrderSort")
    private int orderSort;

    public MediaQueue(String mediaUri, String name, boolean isVideo, int type, int orderSort) {
        this.mediaUri = mediaUri;
        this.name = name;
        this.isVideo = isVideo;
        this.type = type;
        this.orderSort = orderSort;
    }

    public int getOrderSort() {
        return orderSort;
    }

    public void setOrderSort(int orderSort) {
        this.orderSort = orderSort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
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
