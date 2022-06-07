package com.example.mediaplayerapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Size;

import androidx.annotation.DrawableRes;

import java.io.IOException;

public class MediaMetadataUtils {

    /**
     * Get Bitmap thumbnail of a media by its URI.
     * @param context Application context
     * @param uri URI of content
     * @return Bitmap of the thumbnail
     */
    public static Bitmap getThumbnail(Context context, Uri uri, @DrawableRes int defaultImageResourceId) {
        try {
            return context.getApplicationContext()
                    .getContentResolver()
                    .loadThumbnail(uri, new Size(800, 800),null);
        } catch (IOException e) {
            return BitmapFactory.decodeResource(context.getResources(), defaultImageResourceId);
        }
    }

    /**
     * Get display name of a media by its URI.
     * @param context Context
     * @param uri URI of the media
     * @return Display name
     */
    public static String getDisplayName(Context context, Uri uri) {
        String displayName = null;

        String[] projection = new String[] {
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.TITLE
        };

        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            // Prefer using title name in the metadata of
            // the media over the default display name if possible.
            int titleColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE);
            int displayNameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            displayName = cursor.getString(titleColumnIndex);
            if (displayName == null)
                displayName = cursor.getString(displayNameColumnIndex);
            cursor.close();
        }

        // Get the display name the manual way if media store can't find it.
        if (displayName == null)
            displayName = uri.getLastPathSegment();

        return displayName;
    }

    /**
     * Get artist name of an audio media by its URI.
     *
     * @param context Context
     * @param uri URI of the media
     * @return Artist name
     */
    public static String getArtistName(Context context, Uri uri) {
        String artistName = null;

        String[] projection = new String[] {
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            int artistColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            artistName = cursor.getString(artistColumnIndex);
            cursor.close();
        }

        if (artistName != null)
            return artistName;

        // Use the slower metadata retriever as last resort if the media store cannot
        // find the artist name.
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (artistName == null)
            artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        return artistName;
    }
}
