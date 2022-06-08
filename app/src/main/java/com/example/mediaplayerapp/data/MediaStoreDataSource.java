package com.example.mediaplayerapp.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * System's MediaStore API data source.
 */
public class MediaStoreDataSource {

    protected Context context;
    protected Uri contentUri;

    /**
     * Construct MediaStore data source.
     * @param context Application context
     * @param contentUri URI of content to access
     */
    public MediaStoreDataSource(Context context, Uri contentUri) {
        this.context = context.getApplicationContext();
        this.contentUri = contentUri;
    }

    /**
     * Get cursor to iterate over items from query
     * @param projection Columns to get
     * @param selection Filter condition
     * @param selectionArgs Filter arguments
     * @param sortOrder Sort order
     * @return Cursor to iterate over the media items
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

    /**
     * Get cursor to access a media item from its content URI and query.
     * @param uri Content URI
     * @param projection Projection
     * @return Cursor to access the media item
     */
    public Cursor getMediaItem(Uri uri, String[] projection) {
        return context.getContentResolver().query(
                uri, projection, null, null, null
        );
    }

    public Uri getMediaItemUri(long id) {
        return ContentUris.withAppendedId(contentUri, id);
    }
}
