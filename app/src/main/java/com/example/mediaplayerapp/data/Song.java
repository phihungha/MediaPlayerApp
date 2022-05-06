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
