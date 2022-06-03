package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ArtistsMediaStoreDataSource extends MediaStoreDataSource {
    public ArtistsMediaStoreDataSource(Context context) {
        super(context, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get artists that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of Artist objects
     */
    public Observable<List<Artist>> getArtists(String selection, String[] selectionArgs, String sortOrder) {
        return Observable.fromCallable(() -> {
            List<Artist> artists = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Audio.Artists._ID,
                    MediaStore.Audio.Artists.ARTIST,
                    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                    MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            };

            Cursor cursor = getMediaItems(projection,
                    selection,
                    selectionArgs,
                    sortOrder);

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
        });
    }
}
