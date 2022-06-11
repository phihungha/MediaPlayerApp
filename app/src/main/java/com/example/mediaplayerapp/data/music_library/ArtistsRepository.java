package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

/**
 * Get artists.
 */
public class ArtistsRepository {

    public enum SortBy {
        NAME,
        NUMBER_OF_ALBUMS,
        NUMBER_OF_TRACKS
    }

    private final MediaStoreDataSource mediaStore;
    private final SongsRepository songsRepository;

    /**
     * Construct artist repository.
     * @param context Application context
     */
    public ArtistsRepository(Context context) {
        mediaStore = new MediaStoreDataSource(context, MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI);
        songsRepository = new SongsRepository(context);
    }

    /**
     * Get artists that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of Artist objects
     */
    public Single<List<Artist>> getArtists(String selection, String[] selectionArgs, String sortOrder) {
        return Single.fromCallable(() -> {
            List<Artist> artists = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Audio.Artists._ID,
                    MediaStore.Audio.Artists.ARTIST,
                    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                    MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            };

            Cursor cursor = mediaStore.getMediaItems(
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            );

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID);
            int artistColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST);
            int numberOfAlbumsColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int numberOfTracksColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

            if (cursor.moveToFirst()) {
                do {
                    long artistId = cursor.getLong(idColumnIndex);
                    songsRepository.getFirstSongFromArtist(artistId)
                                   .blockingSubscribe(song -> {
                                        Uri thumbnailUri = song.getUri();
                                        artists.add(new Artist(
                                                cursor.getLong(idColumnIndex),
                                                cursor.getString(artistColumnIndex),
                                                cursor.getInt(numberOfAlbumsColumnIndex),
                                                cursor.getInt(numberOfTracksColumnIndex),
                                                thumbnailUri));
                                });
                } while (cursor.moveToNext());
                cursor.close();
            }
            return artists;
        });
    }

    /**
     * Get all artists from media store.
     * @return List of all artists
     */
    public Single<List<Artist>> getAllArtists(SortBy sortBy, SortOrder sortOrder) {
        String sortQuery = "";
        switch (sortBy) {
            case NAME:
                sortQuery = MediaStore.Audio.Artists.ARTIST;
                break;
            case NUMBER_OF_ALBUMS:
                sortQuery = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS;
                break;
            case NUMBER_OF_TRACKS:
                sortQuery = MediaStore.Audio.Artists.NUMBER_OF_TRACKS;
                break;
        }

        switch (sortOrder) {
            case ASC:
                sortQuery += " ASC";
                break;
            case DESC:
                sortQuery += " DESC";
                break;
        }

        return getArtists(null, null, sortQuery);
    }

    public Single<Artist> getArtist(long id) {
        return getArtists(MediaStore.Audio.Artists._ID + " = ?",
                new String[] { String.valueOf(id) },
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER)
                .map(i -> i.get(0));
    }
}
