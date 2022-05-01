package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaUtils {
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy= MediaStore.Video.Media.DATE_TAKEN;

            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, orderBy + " DESC");
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getNameFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy=MediaStore.Video.Media.DATE_TAKEN;

            String[] proj = { MediaStore.Images.Media.DISPLAY_NAME };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, orderBy + " DESC");
            int name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            return cursor.getString(name);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getThumbFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy=MediaStore.Video.Media.DATE_TAKEN;

            String[] proj = { MediaStore.Images.Thumbnails.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, orderBy + " DESC");
            int thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
            cursor.moveToFirst();
            return cursor.getString(thumb);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
