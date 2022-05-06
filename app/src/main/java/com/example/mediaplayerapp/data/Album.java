package com.example.mediaplayerapp.data;

public class Album {public final long id;
    public final String albumName;
    public final long artistId;
    public final String artistName;
    public final int numSong;
    public final int year;


    public Album() {
        id = -1;
        albumName = "";
        artistId = -1;
        artistName = "";
        numSong = -1;
        year = -1;
    }

    public Album(long id, String albumName, long artistId,
                 String artistName, int numSong, int year) {
        this.id = id;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.numSong = numSong;
        this.year = year;
    }
}
