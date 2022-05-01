package com.example.mediaplayerapp.ui.playlist;

public class PlaylistVideoModel {
    private String path,thumb,name;

    public PlaylistVideoModel(String path, String thumb, String name) {
        this.path = path;
        this.thumb = thumb;
        this.name = name;
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
