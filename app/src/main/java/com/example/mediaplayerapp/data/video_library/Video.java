package com.example.mediaplayerapp.data.video_library;

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
    private final String path;
    private final long size;
    private final String resolution;
    private final int dateTaken;

    public Video(Uri uri, String name, int duration, String path, long size, String resolution, int dateTaken) {
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

    public int getDateTaken() {
        return dateTaken;
    }

    public String getPath() {
        return path;
    }
}
