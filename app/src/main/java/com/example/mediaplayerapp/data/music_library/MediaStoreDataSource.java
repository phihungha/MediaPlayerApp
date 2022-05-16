package com.example.mediaplayerapp.data.music_library;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * System's MediaStore API data source.
 */
public class MediaStoreDataSource {
    Context context;
    Uri contentUri;

    /**
     * Construct MediaStore data source.
     * @param context Application context
     * @param contentUri URI of content to access
     */
    public MediaStoreDataSource (Context context, Uri contentUri) {
        this.context = context.getApplicationContext();
        this.contentUri = contentUri;
    }

    /**
     * Get cursor to iterate over items from specified query
     * @param projection Columns to get
     * @param selection Filter condition
     * @param selectionArgs Filter arguments
     * @param sortOrder Sort order
     * @return Cursor of media items
     */
    public Cursor getMediaItems(String[] projection,
                                String selection,
                                String[] selectionArgs,
                                String sortOrder) {
        return context.getContentResolver().query(
            contentUri,
            projection,
            selection,
            selectionArgs,
            sortOrder);
    }

    public Uri getMediaItemUri(long id) {
        return ContentUris.withAppendedId(contentUri, id);
    }
}
