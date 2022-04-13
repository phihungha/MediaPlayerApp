package com.example.mediaplayerapp.data;

import android.graphics.Bitmap;
import android.net.Uri;

public class Video {
    public final Uri uri;
    public final Bitmap thumbNail;
    public final String name;
    public final int duration;

    public Video(Uri uri, Bitmap thumbNail, String name, int duration) {
        this.uri = uri;
        this.thumbNail = thumbNail;
        this.name = name;
        this.duration = duration;
    }
}
