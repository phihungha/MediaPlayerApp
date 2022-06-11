package com.example.mediaplayerapp.data.music_library;

import android.net.Uri;

public class Artist {
    private final long id;
    private final String artistName;
    private final int numberOfSongs;
    private final int numberOfAlbums;
    private final Uri thumbnailUri;

    public Artist(long id, String artistName, int numberOfSongs, int numberOfAlbums, Uri thumbnailUri)
    {
        this.id = id;
        this.artistName = artistName;
        this.numberOfSongs = numberOfSongs;
        this.numberOfAlbums = numberOfAlbums;
        this.thumbnailUri = thumbnailUri;
    }

    public long getId() {
        return id;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }
}
