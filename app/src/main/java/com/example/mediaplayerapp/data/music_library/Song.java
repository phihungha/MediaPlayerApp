package com.example.mediaplayerapp.data.music_library;


import android.net.Uri;

public class Song {
    public long songId;
    private final int libraryIndex;
    private final Uri uri;
    private final String title;
    private final String artistName;
    private final String albumName;
    public long artistId;
    public long albumId;
    private final long duration;

    public Song(long id, Uri uri, String title, long albumId, String albumName,
                long artistId, String artistName, int duration, int libraryIndex) {
        this.songId = id;
        this.uri = uri;
        this.title = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.duration = duration;
        this.libraryIndex = libraryIndex;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public long getDuration() {
        return duration;
    }

    public Uri getUri() {
        return uri;
    }

    public int getLibraryIndex() {
        return libraryIndex;
    }
}
