package com.example.mediaplayerapp.data.music_library;


import android.net.Uri;

public class Song {
    public long songId;
    private Uri uri;
    private String title;
    private String artistName;
    private String albumName;
    public long artistId;
    public long albumId;
    private long duration;
    public int trackNumber;

    public Song(long id, Uri uri, String title, long albumId, String albumName,
                long artistId, String artistName, int duration, int trackNumber) {
        this.songId = id;
        this.setUri(uri);
        this.setTitle(title);
        this.albumId = albumId;
        this.setAlbumName(albumName);
        this.artistId = artistId;
        this.setArtistName(artistName);
        this.setDuration(duration);
        this.trackNumber = trackNumber;
    }

    public void setId(long id) {
        this.songId = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
