package com.example.mediaplayerapp.data.music_library;

import android.net.Uri;

import java.time.ZonedDateTime;

public class Song {
    private final int orderIndex;
    private final Uri uri;
    private final String title;
    private final String artistName;
    private final String albumName;
    private final String genre;
    private final long duration;
    private final ZonedDateTime timeAdded;

    public Song(Uri uri, String title, String albumName, String artistName,
                String genre, int duration, ZonedDateTime timeAdded, int orderIndex) {
        this.uri = uri;
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.genre = genre;
        this.duration = duration;
        this.timeAdded = timeAdded;
        this.orderIndex = orderIndex;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getGenre() {
        return genre;
    }

    public long getDuration() {
        return duration;
    }

    public ZonedDateTime getTimeAdded() {
        return timeAdded;
    }

    public Uri getUri() {
        return uri;
    }

    public int getOrderIndex() {
        return orderIndex;
    }
}
