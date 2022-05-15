package com.example.mediaplayerapp.data;

public class Artist {
    public static final int TYPE_LIST=2;
    public static final int TYPE_GRID=1;
    public int typeDisplay;
    public long ArtistId;
    public String ArtistName;
    public long num_tracks;
    public long num_albums;
    public Artist()
    {
        ArtistId=0;
        ArtistName="";
        num_albums=0;
        num_tracks=0;
    }
    public Artist(long artistId, String artistName, long num_tracks, long num_albums)
    {
        this.ArtistId=artistId;
        this.ArtistName=artistName;
        this.num_tracks=num_tracks;
        this.num_albums=num_albums;
    }
    public void setId(long id) {
        this.ArtistId = id;
    }
    public void setName(String name) {
        this.ArtistName = name;
    }
    public void setNum_tracks(long num_tracks) {
        this.num_tracks = num_tracks;
    }
    public void setNum_albums(long num_albums) {
        this.num_albums = num_albums;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public long getArtistId() {
        return ArtistId;
    }

    public String getArtistName() {
        return ArtistName;
    }
}
