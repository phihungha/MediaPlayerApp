package com.example.mediaplayerapp.data.music_library;

import android.net.Uri;

import com.example.mediaplayerapp.data.MediaItem;

import java.time.ZonedDateTime;

public class Song extends MediaItem {
    private final String artistName;
    private final String albumName;
    private final String genre;

    public Song(Uri uri,
                String title,
                String albumName,
                String artistName,
                String genre,
                int duration,
                ZonedDateTime timeAdded,
                String fileName,
                String location,
                long size,
                int orderIndex) {
        super(uri, title, duration, timeAdded, fileName, location, size, orderIndex);
        this.albumName = albumName;
        this.artistName = artistName;
        this.genre = genre;
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
}
