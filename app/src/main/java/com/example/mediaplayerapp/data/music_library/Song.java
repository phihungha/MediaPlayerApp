package com.example.mediaplayerapp.data.music_library;


import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

public class Song {
    private final int orderIndex;
    private final Uri uri;
    private final String title;
    private final String artistName;
    private final String albumName;
    private final long duration;

    public Song(Uri uri, String title, String albumName,
                String artistName, int duration, int orderIndex) {
        this.uri = uri;
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.duration = duration;
        this.orderIndex = orderIndex;
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

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getGenre(Context context)
    {
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        mr.setDataSource(context,this.uri);
        return  mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
    }
}
