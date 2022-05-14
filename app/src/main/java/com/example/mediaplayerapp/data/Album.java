package com.example.mediaplayerapp.data;

public class Album {public  long id;
    public String albumName;
    public long artistId;
    public String artistName;
    public int numSong;
    public int year;


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
    public void setId(long id) {
        this.id = id;
    }
    public void setAlbumName(String name) {
        this.albumName = name;
    }
    public void setArtistId(long id) {
        this.artistId = id;
    }
    public void setArtistName(String name) {
        this.artistName = name;
    }
    public void setNumSong(int num) {
        this.numSong = num;
    }
    public void setYear(int num) {
        this.year = num;
    }

    public String getAlbumName() {
        return albumName;
    }
}
