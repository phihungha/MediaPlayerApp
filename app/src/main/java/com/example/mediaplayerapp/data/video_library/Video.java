package com.example.mediaplayerapp.data.video_library;

import android.net.Uri;

import com.example.mediaplayerapp.data.MediaItem;

import java.time.ZonedDateTime;

public class Video extends MediaItem {

    private final String resolution;

    public Video(Uri uri,
                 String title,
                 String resolution,
                 int duration,
                 ZonedDateTime timeAdded,
                 String fileName,
                 String location,
                 long size,
                 int orderIndex) {
        super(uri, title, duration, timeAdded, fileName, location, size, orderIndex);
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }
}
