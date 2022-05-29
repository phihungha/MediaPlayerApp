package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class SongsMediaStoreDataSource extends MediaStoreDataSource {
    public SongsMediaStoreDataSource(Context context) {
        super(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get songs that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of Song objects
     */
    public Observable<List<Song>> getSongs(String selection, String[] selectionArgs, String sortOrder) {
        return Observable.fromCallable(() -> {
            List<Song> songs = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATE_ADDED
            };

            String longFormMediaOnlySelection =
                    "(" + MediaStore.Audio.Media.IS_MUSIC + " != 0 OR "
                            + MediaStore.Audio.Media.IS_AUDIOBOOK + " != 0 OR "
                            + MediaStore.Audio.Media.IS_PODCAST + " != 0)";
            if (selection != null && !selection.isEmpty())
                longFormMediaOnlySelection += " AND " + selection;

            Cursor cursor = getMediaItems(
                    projection,
                    longFormMediaOnlySelection,
                    selectionArgs,
                    sortOrder);

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);

            int orderIndex = 0;
            if (cursor.moveToFirst()) {
                do {
                    Uri songUri = getMediaItemUri(cursor.getLong(idColumn));
                    LocalDateTime timeAdded = LocalDateTime.ofEpochSecond(
                            cursor.getInt(dateAddedColumn) * 1000L,
                            0, ZoneOffset.UTC);
                    songs.add(new Song(
                            songUri,
                            cursor.getString(titleColumn),
                            cursor.getString(albumColumn),
                            cursor.getString(artistColumn),
                            getGenreFromSong(songUri),
                            cursor.getInt(durationColumn),
                            timeAdded,
                            orderIndex));
                    orderIndex++;
                } while (cursor.moveToNext());
                cursor.close();
            }

            return songs;
        });
    }

    private String getGenreFromSong(Uri songUri)
    {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, songUri);
        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
    }
}
