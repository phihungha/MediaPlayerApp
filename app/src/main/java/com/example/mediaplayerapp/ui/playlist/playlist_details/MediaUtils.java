package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MediaUtils {
    public static MediaInfo getInfoWithUri(Context context,Uri uri){
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.DURATION,
                            MediaStore.MediaColumns.SIZE,
                            MediaStore.MediaColumns.DATA,
                            MediaStore.MediaColumns.DATE_ADDED};

            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            int column_duration= cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION);
            int column_size = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
            int column_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int column_date = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);
            cursor.moveToFirst();

            String name=cursor.getString(column_name);
            String duration=cursor.getString(column_duration);
            String size=cursor.getString(column_size);
            String data=cursor.getString(column_data);
            String date=cursor.getString(column_date);
            return new MediaInfo(name,duration,size,data,date);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getMediaNameFromURI(Context context, Uri uri) {

        Cursor cursor = null;
        try {
            String[] projection = new String[]{
                    MediaStore.MediaColumns.DISPLAY_NAME,
            };
            cursor = context.getContentResolver().query(uri, projection,
                    null,null,null);
            cursor.moveToFirst();

            int nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            String name = cursor.getString(nameColumnIndex);
            cursor.close();
            return name;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
