package com.example.mediaplayerapp.data;

import android.graphics.Bitmap;

public class Song {
    public String songId;
    public String songTitle;
    public String songArtist;
    public String albumName;
    public String data;
    public String genre;
    public long duration;
    public Bitmap albumArt;
    public Song() {
        songId = "";
        songTitle = "";
        albumName = "";
        data="";
        songArtist = "";
        genre="";
        duration = -1;

    }
    public Song(String id, String title, String albumName,
               String artistName, String data) {
        this.songId = id;
        this.songTitle = title;
        this.albumName = albumName;
        this.songArtist = artistName;
        this.data=data;

    }
    public String toString() {
        return String.format("songId: %d, Title: %s, Artist: %s, Path: %s, Genere: %d, Duration %s",
                songId, songTitle, songArtist, data, genre, duration);
    }
    public void setId(String id) {
        this.songId = id;
    }
    public void setArtist(String artist) {
        this.songArtist = artist;
    }
    public void setTitle(String title) {
        this.songTitle = title;
    }
    public void setData(String data) {
        this.data = data;
    }
    public void setAlbum(String albumName) {
        this.albumName = albumName;
    }
}
