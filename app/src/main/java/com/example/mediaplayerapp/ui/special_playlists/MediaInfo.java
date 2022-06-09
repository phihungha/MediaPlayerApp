package com.example.mediaplayerapp.ui.special_playlists;

import java.io.Serializable;

public class MediaInfo implements Serializable {
    private final String fileName;
    private long duration;
    private final String fileSize;
    private final String location;

    public MediaInfo(String fileName, long duration, String fileSize, String location) {
        this.fileName = fileName;
        this.duration = duration;
        this.fileSize = fileSize;
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getLocation() {
        return location;
    }

}
