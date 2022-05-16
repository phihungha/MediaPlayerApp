package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.provider.MediaStore;

import java.util.List;

/**
 * Get albums.
 */
public class AlbumRepository {
    AlbumMediaStoreDataSource mediaStore;

    /**
     * Construct album repository.
     * @param context Application context
     */
    public AlbumRepository(Context context) {
        this.mediaStore = new AlbumMediaStoreDataSource(context);
    }

    public List<Album> getAllAlbums() {
        return mediaStore.getAlbums(null, null);
    }

    public Album getAlbum(long id) {
        return mediaStore.getAlbums(MediaStore.Audio.Albums._ID + " = ?",
                new String[] { String.valueOf(id) }).get(0);
    }
}
