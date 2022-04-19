package com.example.mediaplayerapp.data;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Comparator;

public class Video {

    public static Comparator<Video> VideoNameAZComparator = new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            return video1.getName().compareTo(video2.getName());
        }
    };
    public static Comparator<Video> VideoNameZAComparator = new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            return video2.getName().compareTo(video1.getName());
        }
    };
    public static Comparator<Video> VideoDurationAscendingComparator = new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            return video1.getDuration() - video2.getDuration();
        }
    };
    public static Comparator<Video> VideoDurationDescendingComparator = new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            return video2.getDuration() - video1.getDuration();
        }
    };

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
