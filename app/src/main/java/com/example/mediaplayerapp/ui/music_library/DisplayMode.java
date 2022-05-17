package com.example.mediaplayerapp.ui.music_library;

public enum DisplayMode {
    GRID(0),
    LIST(1);

    private final int value;
    DisplayMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
