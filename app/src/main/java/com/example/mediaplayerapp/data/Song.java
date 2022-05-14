package com.example.mediaplayerapp.data;

import android.graphics.Bitmap;

public class Song {
    public long songId;
    public String songTitle;
    public String songArtist;
    public String albumName;
    public long artistId;
    public long albumId;
    public long duration;
    public int trackNumber;
    public Song() {
        songId = 0;
        songTitle = "";
        albumName = "";
        albumId=0;
        songArtist = "";
        albumId=0;
        duration = -1;

    }
    public Song(long id, String title, long albumId, String albumName,
                long artistId, String artistName, int duration, int trackNumber) {
        this.songId = id;
        this.songTitle = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.songArtist = artistName;
        this.duration = duration;
        this.trackNumber = trackNumber;
    }

    public void setId(long id) {
        this.songId = id;
    }
    public void setTitle(String title) {
        this.songTitle = title;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
    public void setAlbum(String albumName) {
        this.albumName = albumName;
    }
    public void setArtistId(long data) {
        this.artistId = data;
    }
    public void setArtist(String artist) {
        this.songArtist = artist;
    }
    public void setDuration(long duration){this.duration=duration;}
    public void setTrackNumber(int trackNumber){this.trackNumber=trackNumber;}

    public long getAlbumId() {
        return albumId;
    }

    public String getSongTitle() {
        return songTitle;
    }
}
