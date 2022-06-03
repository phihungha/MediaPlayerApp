package com.example.mediaplayerapp.utils;

/**
 * Interface for starting playback from a media order index
 * come from an adapter.
 */
public interface StartPlaybackCallback {
    void play(int orderIndex);
}
