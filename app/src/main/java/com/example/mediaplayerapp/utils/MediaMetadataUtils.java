package com.example.mediaplayerapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Size;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.DecimalFormat;

public class MediaMetadataUtils {

    /**
     * Get Bitmap thumbnail of a media by its URI.
     *
     * @param context Application context
     * @param uri     URI of content
     * @return Bitmap of the thumbnail
     */
    public static Drawable getThumbnail(Context context, Uri uri, @DrawableRes int defaultImageResourceId) {
        try {
            Bitmap thumbnailBitmap
                    = context
                    .getContentResolver()
                    .loadThumbnail(uri, new Size(1000, 1000), null);
            return new BitmapDrawable(context.getResources(), thumbnailBitmap);
        } catch (IOException e) {
            return ContextCompat.getDrawable(context, defaultImageResourceId);
        }
    }

    /**
     * Format a file's size into an easy-to-look string format
     *
     * @param size The size of the file
     * @return The file's size as the formatted string
     */
    public static String getFileSizeInString(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
