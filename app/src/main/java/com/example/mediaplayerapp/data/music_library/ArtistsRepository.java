package com.example.mediaplayerapp.data.music_library;

import android.content.Context;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Get artists.
 */
public class ArtistsRepository {
    ArtistMediaStoreDataSource dataSource;

    /**
     * Construct artist repository.
     * @param context Application context
     */
    public ArtistsRepository(Context context) {
        this.dataSource = new ArtistMediaStoreDataSource(context);
    }

    /**
     * Get all artists from media store.
     * @return List of all artists
     */
    public List<Artist> getAllArtists() {
        return dataSource.getArtists(null, null);
    }

    public Artist getArtist(long id) {
        return dataSource.getArtists(MediaStore.Audio.Artists._ID + " = ?",
                new String[] { String.valueOf(id) }).get(0);
    }

    public MutableLiveData<List<Artist>> getArtistSortbyNameDESC()
    {
        return dataSource.getArtistSortbyNameDESC();
    }
    public MutableLiveData<List<Artist>> getArtistSortbyNameASC()
    {
        return dataSource.getArtistSortbyNameASC();
    }

}
