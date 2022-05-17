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
public interface MediaItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaItem media);

    @Update
    void update(MediaItem media);

    @Delete
    void delete(MediaItem media);

    @Query("DELETE FROM media_table")
    void deleteAll();

    @Query("DELETE FROM media_table WHERE media_table.MediaUri=:uri")
    void deleteItemWithUri(String uri);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId= :id")
    LiveData<List<MediaItem>> getAllPlaylistMediasWithID(int id);

    @Query("SELECT * FROM media_table ORDER BY MediaId ASC")
    LiveData<List<MediaItem>> getAllPlaylistMedias();

    @Query("SELECT * FROM media_table WHERE media_table.MediaName LIKE '%' || :text || '%'")
    LiveData<List<MediaItem>> getAllMediaSearching(String text);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId=:id ORDER BY media_table.MediaName ASC")
    LiveData<List<MediaItem>> sortAllMediaByNameASCWithID(int id);

    @Query("SELECT * FROM media_table WHERE media_table.MediaId=:id ORDER BY media_table.MediaName DESC")
    LiveData<List<MediaItem>> sortAllMediaByNameDESCWithID(int id);

    @Query("DELETE FROM media_table WHERE media_table.MediaId=:id")
    void deleteAllWithID(int id);

    @Query("SELECT COUNT(*) FROM media_table WHERE media_table.MediaId=:id")
    int getCountPlaylistWithID(int id);

}
