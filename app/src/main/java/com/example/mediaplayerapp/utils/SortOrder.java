package com.example.mediaplayerapp.utils;

public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    private final String uriSegmentName;

    SortOrder(String uriSegmentName) {
        this.uriSegmentName = uriSegmentName;
    }

    public String getUriSegmentName() {
        return uriSegmentName;
    }
}
