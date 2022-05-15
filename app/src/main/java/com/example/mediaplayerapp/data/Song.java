package com.example.mediaplayerapp.data;



public class Song {
    public static final int TYPE_LIST=1;
    public static final int TYPE_GRID=2;
    public int typeDisplay;
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



    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public long getAlbumId() {
        return albumId;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public String getSongTitle() {
        return songTitle;
    }
}
