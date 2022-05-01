package com.example.mediaplayerapp.ui.playlist.playlist_details;

public class PlaylistMediaModel {
    private String path,thumb,name;
    private int id;

    public PlaylistMediaModel(String path, String thumb, String name) {
        this.path = path;
        this.thumb = thumb;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
