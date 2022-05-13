package com.example.mediaplayerapp.data.playlist.playlist_details;

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

    @Query("SELECT * FROM media_table WHERE media_table.MediaName LIKE '%' || :text || '%'")
    LiveData<List<PlaylistMedia>> getAllMediaSearching(String text);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId=:id ORDER BY media_table.MediaName ASC")
    LiveData<List<PlaylistMedia>> sortAllMediaByNameASCWithID(int id);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId=:id ORDER BY media_table.MediaName DESC")
    LiveData<List<PlaylistMedia>> sortAllMediaByNameDESCWithID(int id);

    @Query("DELETE FROM media_table WHERE media_table.MediaId=:id")
    void deleteAllWithID(int id);

    @Query("SELECT COUNT(*) FROM media_table WHERE media_table.MediaId=:id")
    int getCountPlaylistWithID(int id);

}
