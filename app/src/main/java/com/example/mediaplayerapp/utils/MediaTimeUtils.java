package com.example.mediaplayerapp.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    public static String getFormattedTimeFromLong(long position) {
        long playedSeconds = position / 1000;
        return String.format(Locale.getDefault(), MEDIA_TIME_FORMAT,
                (playedSeconds % 3600) / 60, playedSeconds % 60);
    }

    /**
     * Get ISO formatted time from a LocalDateTime.
     * @param time Time
     * @return ISO formatted time
     */
    public static String getFormattedTimeFromZonedDateTime(ZonedDateTime time) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(time);
    }

    /**
     * Get date time in current time zone from a long value.
     * @param longDateTime Date time as a long value
     * @return Zoned date time
     */
    public static ZonedDateTime getZonedDateTimeFromLong(long longDateTime) {
        Instant instant = Instant.now();
        ZoneId currentZone = ZoneId.systemDefault();
        ZoneOffset currentOffset = currentZone.getRules().getOffset(instant);
        LocalDateTime localTimeAdded = LocalDateTime.ofEpochSecond(
                longDateTime,0, currentOffset);
        return localTimeAdded.atZone(currentZone);
    }
}
