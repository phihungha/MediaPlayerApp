package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class ArtistMediaStoreDataSource extends MediaStoreDataSource {
    public ArtistMediaStoreDataSource(Context context) {
        super(context, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get artists that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @return List of Artist objects
     */
    public List<Artist> getArtists(String selection, String[] selectionArgs) {
        String[] projection = new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        };

        List<Artist> artists = new ArrayList<>();

        Cursor cursor = getMediaItems(projection,
                selection,
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST);
        int numberOfAlbumsColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
        int numberOfTracksColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

        if (cursor.moveToFirst()) {
            do {
                artists.add(new Artist(
                        cursor.getLong(idColumn),
                        cursor.getString(artistColumn),
                        cursor.getInt(numberOfAlbumsColumn),
                        cursor.getInt(numberOfTracksColumn)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return artists;
    }
}
