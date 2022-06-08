package com.example.mediaplayerapp.data.music_library;

public class Artist {
    private final long id;
    private final String artistName;
    private final int numberOfSongs;
    private final int numberOfAlbums;

    public Artist(long id, String artistName, int numberOfSongs, int numberOfAlbums)
    {
        this.id = id;
        this.artistName = artistName;
        this.numberOfSongs = numberOfSongs;
        this.numberOfAlbums = numberOfAlbums;
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
}
