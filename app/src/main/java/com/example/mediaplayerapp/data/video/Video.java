package com.example.mediaplayerapp.data.video;

import android.net.Uri;

import java.util.Comparator;

public class Video {

    public static Comparator<Video> VideoNameAZComparator =
            Comparator.comparing(Video::getName);

    public static Comparator<Video> VideoNameZAComparator =
            (video1, video2) -> video2.getName().compareTo(video1.getName());

    public static Comparator<Video> VideoDurationAscendingComparator =
            Comparator.comparingInt(Video::getDuration);

    public static Comparator<Video> VideoDurationDescendingComparator =
            (video1, video2) -> video2.getDuration() - video1.getDuration();

    private final Uri uri;
    private final String name;
    private final int duration;

    public Video(Uri uri, String name, int duration) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
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
}
