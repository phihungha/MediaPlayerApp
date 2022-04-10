package com.example.mediaplayerapp.data;

import android.net.Uri;

public class Video {
    public final Uri uri;
    public final String name;
    public final int duration;

    public Video(Uri uri, String name, int duration) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
    }
}
