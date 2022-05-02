package com.example.mediaplayerapp.data;

import android.graphics.Bitmap;

public class Song {
    public long songId;
    public String songTitle;
    public String songArtist;
    public String albumName;
    public String path;
    public String genre;
    public long duration;
    public Bitmap albumArt;
    public Song() {
        songId = -1;
        songTitle = "";
        albumName = "";
        path="";
        songArtist = "";
        genre="";
        duration = -1;

    }
    public Song(long id, String title, String albumName,
               String artistName, String path) {
        this.songId = id;
        this.songTitle = title;
        this.albumName = albumName;
        this.songArtist = artistName;
        this.path=path;

    }
    public String toString() {
        return String.format("songId: %d, Title: %s, Artist: %s, Path: %s, Genere: %d, Duration %s",
                songId, songTitle, songArtist, path, genre, duration);
    }

}
