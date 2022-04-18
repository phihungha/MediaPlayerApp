package com.example.mediaplayerapp.data;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Comparator;

public class Video {
    private final Uri uri;
    private final Bitmap thumbNail;
    private final String name;
    private final int duration;

    public Video(Uri uri, Bitmap thumbNail, String name, int duration) {
        this.uri = uri;
        this.thumbNail = thumbNail;
        this.name = name;
        this.duration = duration;
    }

    public Uri getUri() {
        return uri;
    }

    public Bitmap getThumbNail() {
        return thumbNail;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}
