package com.example.mediaplayerapp.data.music_library;

public class Album {
    public static final int TYPE_LIST=2;
    public static final int TYPE_GRID=1;
    public int typeDisplay;
    public  long id;
    public String albumName;
    public String artistName;
    public int numberOfSongs;
    public int year;

    public Album(long id, String albumName, String artistName, int numberOfSongs, int year) {
        this.id = id;
        this.albumName = albumName;
        this.artistName = artistName;
        this.numberOfSongs = numberOfSongs;
        this.year = year;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public long getId() {
        return id;
    }

    public String getAlbumName() {
        return albumName;
    }
}
