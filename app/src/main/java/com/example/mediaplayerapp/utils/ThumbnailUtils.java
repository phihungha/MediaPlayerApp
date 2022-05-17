package com.example.mediaplayerapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Size;

import java.io.IOException;

public class ThumbnailUtils {
    /**
     * Get a Bitmap of thumbnail for the specified URI's content.
     * @param context Application context
     * @param uri URI of content
     * @return Bitmap of the thumbnail
     * @throws IOException Thumbnail not found
     */
    public static Bitmap getThumbnailFromUri(Context context, Uri uri) throws IOException {
        return context.getApplicationContext().getContentResolver()
                .loadThumbnail(uri, new Size(500, 500),null);
    }
}
