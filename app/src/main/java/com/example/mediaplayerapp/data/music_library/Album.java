package com.example.mediaplayerapp.data.music_library;

import android.net.Uri;

public class Album {
    private long id;
    private final Uri uri;
    private final String albumName;
    private final String artistName;
    private final int numberOfSongs;
    private final int year;

    public Album(long id, Uri uri, String albumName, String artistName, int numberOfSongs, int year) {
        this.id = id;
        this.uri = uri;
        this.albumName = albumName;
        this.artistName = artistName;
        this.numberOfSongs = numberOfSongs;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public int getYear() {
        return year;
    }

    public Uri getUri() {
        return uri;
    }
}
