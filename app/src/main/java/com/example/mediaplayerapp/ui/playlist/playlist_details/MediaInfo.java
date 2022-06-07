package com.example.mediaplayerapp.ui.playlist.playlist_details;

import java.io.Serializable;

public class MediaInfo implements Serializable {
    private final String fileName;
    private String duration;
    private final String fileSize;
    private final String location;

    public MediaInfo(String fileName, String duration, String fileSize, String location) {
        this.fileName = fileName;
        this.duration = duration;
        this.fileSize = fileSize;
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getLocation() {
        return location;
    }

}
