package com.example.mediaplayerapp.data;

import android.net.Uri;

import java.time.ZonedDateTime;

public abstract class MediaItem {

    protected final Uri uri;
    protected final String title;
    protected final int duration;
    protected final ZonedDateTime timeAdded;
    protected final String fileName;
    protected final String location;
    protected final long size;
    protected final int orderIndex;

    protected MediaItem(Uri uri,
                        String title,
                        int duration,
                        ZonedDateTime timeAdded,
                        String fileName,
                        String location,
                        long size,
                        int orderIndex) {
        this.uri = uri;
        this.title = title;
        this.duration = duration;
        this.timeAdded = timeAdded;
        this.fileName = fileName;
        this.location = location;
        this.size = size;
        this.orderIndex = orderIndex;
    }

    public Uri getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public ZonedDateTime getTimeAdded() {
        return timeAdded;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLocation() {
        return location;
    }

    public long getSize() {
        return size;
    }

    public int getOrderIndex() {
        return orderIndex;
    }
}
