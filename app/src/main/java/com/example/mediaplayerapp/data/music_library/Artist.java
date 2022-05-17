package com.example.mediaplayerapp.data.music_library;

import android.net.Uri;

public class Artist {
    private final long artistId;
    private final String artistName;
    private final int numberOfSongs;
    private final int numberOfAlbums;

    public Artist(long artistId, String artistName, int numberOfSongs, int numberOfAlbums)
    {
        this.artistId = artistId;
        this.artistName = artistName;
        this.numberOfSongs = numberOfSongs;
        this.numberOfAlbums = numberOfAlbums;
    }

    public long getArtistId() {
        return artistId;
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

}
