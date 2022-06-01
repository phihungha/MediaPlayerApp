package com.example.mediaplayerapp.data.video_library;

import android.net.Uri;

public class Video {

    private final Uri uri;
    private final String name;
    private final int duration;
    private final String path;
    private final long size;
    private final String resolution;
    private final long dateTaken;

    public Video(Uri uri, String name, int duration, String path, long size, String resolution, long dateTaken) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.path = path;
        this.size = size;
        this.resolution = resolution;
        this.dateTaken = dateTaken;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getResolution() {
        return resolution;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public String getPath() {
        return path;
    }
}
