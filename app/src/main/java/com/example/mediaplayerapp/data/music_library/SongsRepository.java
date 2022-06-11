package com.example.mediaplayerapp.data.music_library;

import static com.example.mediaplayerapp.utils.MediaTimeUtils.getZonedDateTimeFromLong;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mediaplayerapp.data.MediaStoreDataSource;
import com.example.mediaplayerapp.utils.SortOrder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Get songs.
 */
public class SongsRepository {

    public enum SortBy {
        TITLE("title"),
        DURATION("duration"),
        TIME_ADDED("time_added");

        private final String uriSegmentName;

        SortBy(String uriSegmentName) {
            this.uriSegmentName = uriSegmentName;
        }

        public String getUriSegmentName() {
            return uriSegmentName;
        }
    }

    private final MediaStoreDataSource mediaStore;

    /**
     * Construct song repository.
     * @param context Application context
     */
    public SongsRepository(Context context) {
        mediaStore = new MediaStoreDataSource(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * Get songs that satisfy selection conditions
     * @param selection SQL selection conditions
     * @param selectionArgs Selection arguments
     * @param sortOrder Sort order
     * @return List of Song objects
     */
    public Single<List<Song>> getSongs(String selection, String[] selectionArgs, String sortOrder) {
        return Single.fromCallable(() -> {
            List<Song> songs = new ArrayList<>();

            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ARTIST,
                    MediaStore.Audio.Media.GENRE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.SIZE
            };

            String longFormMediaOnlySelection =
                    "(" + MediaStore.Audio.Media.IS_MUSIC + " != 0 OR "
                            + MediaStore.Audio.Media.IS_AUDIOBOOK + " != 0 OR "
                            + MediaStore.Audio.Media.IS_PODCAST + " != 0)";
            if (selection != null && !selection.isEmpty())
                longFormMediaOnlySelection += " AND " + selection;

            Cursor cursor = mediaStore.getMediaItems(
                    projection,
                    longFormMediaOnlySelection,
                    selectionArgs,
                    sortOrder
            );

            int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int displayNameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int titleColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int albumColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int artistColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumArtistColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST);
            int genreColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE);
            int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int dateAddedColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);
            int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);

            int orderIndex = 0;
            if (cursor.moveToFirst()) {
                do {
                    Uri songUri = mediaStore.getMediaItemUri(cursor.getLong(idColumnIndex));

                    String title = cursor.getString(titleColumnIndex);
                    String fileName = cursor.getString(displayNameColumnIndex);
                    // Use file name if the song doesn't have a title
                    if (title == null)
                        title = fileName;

                    String artist = cursor.getString(artistColumnIndex);
                    // Use album artist name if the song doesn't have artist name
                    if (artist == null)
                        artist = cursor.getString(albumArtistColumnIndex);

                    songs.add(new Song(
                            songUri,
                            title,
                            cursor.getString(albumColumnIndex),
                            artist,
                            cursor.getString(genreColumnIndex),
                            cursor.getInt(durationColumnIndex),
                            getZonedDateTimeFromLong(cursor.getLong(dateAddedColumnIndex)),
                            fileName,
                            cursor.getString(dataColumnIndex),
                            cursor.getLong(sizeColumnIndex),
                            orderIndex));
                    orderIndex++;
                } while (cursor.moveToNext());
                cursor.close();
            }

            return songs;
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Get all songs from media store.
     * @return List of songs
     */
    public Single<List<Song>> getAllSongs(SortBy sortBy, SortOrder sortOrder) {
        String sortQuery = "";
        switch (sortBy) {
            case TITLE:
                sortQuery = MediaStore.Audio.Media.TITLE;
                break;
            case DURATION:
                sortQuery = MediaStore.Audio.Media.DURATION;
                break;
            case TIME_ADDED:
                sortQuery = MediaStore.Audio.Media.DATE_ADDED;
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

        return getSongs(null, null, sortQuery);
    }

    public Single<Song> getSong(Uri uri) {
        return getSongs(MediaStore.Audio.Media._ID + " = ?",
                new String[] { String.valueOf(ContentUris.parseId(uri)) },
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
                .map(i -> i.get(0));
    }

    /**
     * Get all songs from an artist.
      * @param artistId Id of the artist
     * @return List of songs
     */
    public Single<List<Song>> getAllSongsFromArtist(long artistId) {
        return getSongs(MediaStore.Audio.Media.ARTIST_ID + " = ?",
                new String[] { String.valueOf(artistId) },
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    /**
     * Get first song from an artist for thumbnail display.
     * @param artistId Id of the artist
     * @return List of songs
     */
    public Single<Song> getFirstSongFromArtist(long artistId) {
        return getAllSongsFromArtist(artistId).map(i -> i.get(0));
    }

    /**
     * Get all songs from an album.
     * @param albumId Id of the album
     * @return List of songs
     */
    public Single<List<Song>> getAllSongsFromAlbum(long albumId) {
        return getSongs(MediaStore.Audio.Media.ALBUM_ID + " = ?",
                new String[] { String.valueOf(albumId) },
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }
}
