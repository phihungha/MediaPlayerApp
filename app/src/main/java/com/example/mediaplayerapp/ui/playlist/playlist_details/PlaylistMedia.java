package com.example.mediaplayerapp.ui.playlist.playlist_details;

/***
 *  Video class for playlist
 */
public class PlaylistMedia {
    private String videoUri;
    private String name;

    public PlaylistMedia(String videoUri, String name) {
        this.videoUri = videoUri;
        this.name = name;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
