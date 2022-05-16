package com.example.mediaplayerapp.utils;

import java.util.Locale;

/**
 * Utility methods to handle time values related to media.
 */
public class MediaTimeUtils {
    private static final String MEDIA_TIME_FORMAT = "%02d:%02d";

    /**
     * Get formatted time from a millisecond value.
     * @param position Playback position as milliseconds
     * @return String Formatted playback position
     */
    public static String getFormattedTime(long position) {
        long playedSeconds = position / 1000;
        return String.format(Locale.getDefault(), MEDIA_TIME_FORMAT,
                (playedSeconds % 3600) / 60, playedSeconds % 60);
    }
}
