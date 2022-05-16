package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.provider.MediaStore;

import java.util.List;

/**
 * Get songs.
 */
public class SongRepository {
    SongMediaStoreDataSource mediaStore;

    /**
     * Construct song repository.
     * @param context Application context
     */
    public SongRepository(Context context) {
        this.mediaStore = new SongMediaStoreDataSource(context);

    }

    /**
     * Get all songs from media store.
     * @return List of all songs
     */
    public List<Song> getAllSongs() {
        return mediaStore.getSongs(null, null);
    }

    /**
     * Get all songs from an artist.
      * @param artistId Id of the artist
     * @return List of Song objects
     */
    public List<Song> getAllSongsFromArtist(long artistId) {
        return mediaStore.getSongs(MediaStore.Audio.Media.ARTIST_ID + " = ?",
                new String[] { String.valueOf(artistId) });
    }

    /**
     * Get all songs from an album.
     * @param albumId Id of the album
     * @return List of Song objects
     */
    public List<Song> getAllSongsFromAlbum(long albumId) {
        return mediaStore.getSongs(MediaStore.Audio.Media.ALBUM_ID + " = ?",
                new String[] { String.valueOf(albumId) });
    }
}
