package com.example.mediaplayerapp.ui.special_playlists;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import java.util.Objects;

public class MediaUtils {
    public static MediaInfo getInfoWithUri(@NonNull Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE
            };

            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            cursor.moveToFirst();

            int column_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            int column_size = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

            String name = cursor.getString(column_name);
            String size = cursor.getString(column_size);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(context, uri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMilliSec = Long.parseLong(time);
            retriever.release();

            return new MediaInfo(name, timeInMilliSec, size, uri.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap loadThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        byte[] data = retriever.getEmbeddedPicture();
        retriever.release();
        if (data == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static boolean isUriExists(Context context, Uri uri) {
        return Objects.requireNonNull(DocumentFile.fromSingleUri(context, uri)).exists();
    }

    public static long getDurationFromUri(Context context, Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, uri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMilliSec = Long.parseLong(time);
        retriever.release();
        return timeInMilliSec;
    }

    public static String getMediaNameFromURI(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = new String[]{
                    MediaStore.MediaColumns.DISPLAY_NAME,
            };
            cursor = context.getContentResolver().query(uri, projection,
                    null, null, null);
            cursor.moveToFirst();

            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            String name = cursor.getString(nameColumnIndex);
            cursor.close();
            return name;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static long generateOrderSort() {
        long primeNum = 282589933;
        long time = System.currentTimeMillis();
        return time % primeNum;
    }

}
