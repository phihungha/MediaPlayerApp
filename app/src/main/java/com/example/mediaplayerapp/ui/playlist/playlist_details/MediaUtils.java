package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;

import java.util.List;
import java.util.Objects;

public class MediaUtils {
    public static MediaInfo getInfoWithUri(@NonNull Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.MediaColumns.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE};

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
            String duration = String.valueOf(timeInMilliSec);

            return new MediaInfo(name, duration, size, uri.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap loadThumbnail(Context context, Uri uri) {
        context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        byte[] data = retriever.getEmbeddedPicture();
        retriever.release();
        if (data == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    public static String convertDuration(String duration) {
        long l = Long.parseLong(duration);
        long hour = l / (1000 * 60 * 60);
        long min = l / (1000 * 60) % 60;
        long sec = l / 1000 % 60;

        String sHour = String.valueOf(hour);
        String sMin = String.valueOf(min);
        String sSec = String.valueOf(sec);

        if (min < 10) {
            sMin = "0" + sMin;
        }
        if (sec < 10) {
            sSec = "0" + sSec;
        }
        if (hour == 0) {
            return sMin + ":" + sSec;
        } else {
            return sHour + ":" + sMin + ":" + sSec;
        }
    }

    public static String convertToSizeMb(String size) {
        long s = Long.parseLong(size) / (1024 * 1024);
        if (s > 0) {
            double d = (double) Math.round(s * 1000) / 1000;
            size = d + " Mb";
        } else {
            s = Long.parseLong(size) / 1024;
            if (s > 0) {
                double d = (double) Math.round(s * 1000) / 1000;
                size = d + " Kb";
            } else {
                s = Long.parseLong(size);
                double d = (double) Math.round(s * 1000) / 1000;
                size = d + " byte";
            }

        }
        return size;

    }

    public static String getMediaNameFromURI(Context context, Uri uri) {
        //context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
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

    public static void updateListToDatabase(Application application, List<PlaylistItem> list) {
        PlaylistItemViewModel viewModel = new PlaylistItemViewModel(application);
        viewModel.updateByList(list);
    }

    public static void insertionSort(Application application, List<PlaylistItem> list, int from, int to) {
        PlaylistItemViewModel viewModel = new PlaylistItemViewModel(application);

        if (from < to) {
            long key = list.get(to).getOrderSort();
            for (int i = to; i > from; i--) {
                PlaylistItem item = list.get(i);
                PlaylistItem pre = list.get(i - 1);
                item.setOrderSort(pre.getOrderSort());
                viewModel.update(item);
            }

            PlaylistItem lastItem = list.get(from);
            lastItem.setOrderSort(key);
            viewModel.update(lastItem);
        }
    }

    public static long generateOrderSort() {
        long primeNum = 282589933;
        long time = System.currentTimeMillis();
        return time % primeNum;
    }

}
