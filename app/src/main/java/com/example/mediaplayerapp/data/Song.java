package com.example.mediaplayerapp.data;

public class Song {
    public final long id;
    public final String title;
    public final long albumId;
    public final String albumName;
    public final long artistId;
    public final String artistName;
    public final int duration;
    public final int trackNumber;


    public Song() {
        id = -1;
        title = "";
        albumId = -1;
        albumName = "";
        artistId = -1;
        artistName = "";
        duration = -1;
        trackNumber = -1;
    }
    public Song(long id, String title, long albumId, String albumName,
                long artistId, String artistName, int duration, int trackNumber) {
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.duration = duration;
        this.trackNumber = trackNumber;
    }

}
