package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.example.mediaplayerapp.data.Video;

import java.util.ArrayList;
import java.util.List;

public class MediaUtils {
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String[] proj = {MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(contentUri, proj, null, null, orderBy + " DESC");
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
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String[] proj = {MediaStore.Images.Media.DISPLAY_NAME};

            cursor = context.getContentResolver().query(contentUri, proj, null, null, orderBy + " DESC");
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
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String[] proj = {MediaStore.Images.Thumbnails.DATA};

            cursor = context.getContentResolver().query(contentUri, proj, null, null, orderBy + " DESC");
            int thumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
            cursor.moveToFirst();
            return cursor.getString(thumb);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Video getVideoFromURI(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = new String[]{
                    MediaStore.MediaColumns._ID,
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    MediaStore.Video.Media.DURATION
            };
            cursor = context.getContentResolver().query(uri, projection,
                    null,null,null);

            cursor.moveToFirst();

            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

            String videoName = cursor.getString(nameColumnIndex);
            int videoDuration = cursor.getInt(durationColumnIndex);

            cursor.close();
            return new Video(uri, videoName, videoDuration);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
