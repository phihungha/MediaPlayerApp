package com.example.mediaplayerapp.ui.playlist.playlist_details;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaylistMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaylistMedia media);

    @Update
    void update(PlaylistMedia media);

    @Delete
    void delete(PlaylistMedia media);

    @Query("DELETE FROM media_table")
    void deleteAll();

    @Query("SELECT * FROM media_table WHERE media_table.MediaId= :id")
    LiveData<List<PlaylistMedia>> getAllPlaylistMediasWithID(int id);

    @Query("SELECT * FROM media_table ORDER BY MediaId ASC")
    LiveData<List<PlaylistMedia>> getAllPlaylistMedias();

}
