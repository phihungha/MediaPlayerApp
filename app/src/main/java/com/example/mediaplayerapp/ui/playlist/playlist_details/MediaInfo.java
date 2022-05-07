package com.example.mediaplayerapp.ui.playlist.playlist_details;

import java.io.Serializable;

public class MediaInfo implements Serializable {
    private String fileName;
    private String duration;
    private String fileSize;
    private String location;
    private String date;

    public MediaInfo(String fileName, String duration, String fileSize, String location, String date) {
        this.fileName = fileName;
        this.duration = duration;
        this.fileSize = fileSize;
        this.location = location;
        this.date = date;
    }

    public MediaInfo() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
