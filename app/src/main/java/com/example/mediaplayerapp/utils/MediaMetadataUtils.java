package com.example.mediaplayerapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MediaMetadataUtils {

    /**
     * Get media name from URI using MediaMetadataRetriever and MediaStore (in case
     * MediaMetadataRetriever doesn't work)
     *
     * @param context The context for MediaMetadataRetriever and getContentResolver() to use
     * @param uri     The URI for MediaMetadataRetriever and getContentResolver() to use
     * @return The name of the media
     */
    public static String getMediaNameFromUri(Context context, Uri uri) {

        MediaMetadataRetriever mediaMetadataRetriever;
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, uri);
        String mediaName = "";
        try {
            mediaName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("[EXCEPTION]",
                    "An exception occurred in MediaMetadataUtils.getMediaNameFromUri() ");
        }

        // In some cases, MediaMetadataRetriever can't retrieve a media's name, so we use
        // MediaStore instead
        if (mediaName == null) {
            String[] projection = new String[]{MediaStore.MediaColumns.DISPLAY_NAME,};

            Cursor cursor = context.getContentResolver().query(uri, projection,
                    null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                mediaName = cursor.getString(nameColumnIndex);
                cursor.close();
            }

        }
        if (mediaName == null) Log.e(
                "[GENERAL_ERROR]",
                "Failed to retrieve media name using MediaMetadataUtils.getMediaNameFromUri for URI:" + uri.toString());
        return mediaName;
    }

    /**
     * Get media artist (mainly for songs) from URI using MediaMetadataRetriever
     *
     * @param context The context for MediaMetadataRetriever to use
     * @param uri     The URI for MediaMetadataRetriever to use
     * @return The media artist
     */
    public static String getMediaArtistFromUri(Context context, Uri uri) {

        MediaMetadataRetriever mediaMetadataRetriever;
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, uri);
        String mediaArtist = "";
        try {
            mediaArtist =
                    mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(
                    "[EXCEPTION]",
                    "An exception occurred in MediaMetadataUtils.getMediaNameFromUri() ");
        }

        if (mediaArtist == null) Log.e(
                "[GENERAL_ERROR]",
                "Failed to retrieve media artist using MediaMetadataUtils.getMediaArtistFromUri() for URI:" + uri.toString());
        return mediaArtist;
    }

}
