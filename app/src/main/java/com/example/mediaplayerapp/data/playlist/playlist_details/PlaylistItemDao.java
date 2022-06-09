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
public interface PlaylistItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaylistItem media);

    @Update
    void update(PlaylistItem media);

    @Delete
    void delete(PlaylistItem media);

    @Query("DELETE FROM PlaylistItems")
    void deleteAll();

    @Query("DELETE FROM PlaylistItems WHERE PlaylistItems.MediaUri=:uri")
    void deleteItemWithUri(String uri);

    @Query("SELECT * FROM PlaylistItems WHERE PlaylistItems.PlaylistItemId= :id ORDER BY OrderSort ASC")
    LiveData<List<PlaylistItem>> getAllPlaylistMediasWithID(int id);

    @Query("SELECT * FROM PlaylistItems ORDER BY OrderSort ASC")
    LiveData<List<PlaylistItem>> getAllPlaylistMedias();

    @Query("DELETE FROM PlaylistItems WHERE PlaylistItems.PlaylistItemId=:id")
    void deleteAllWithID(int id);

    @Query("SELECT COUNT(*) FROM PlaylistItems WHERE PlaylistItems.PlaylistItemId=:id")
    int getCountPlaylistWithID(int id);

    @Query("SELECT * FROM PlaylistItems WHERE PlaylistItems.PlaylistItemId = :id ORDER BY OrderSort ASC")
    PlaylistItem findByItemId(int id);

    @Update
    void updateMultiple(PlaylistItem... playlistItems);

    @Update
    void updateByList(List<PlaylistItem> list);

    @Query("SELECT * FROM PlaylistItems WHERE PlaylistItemId=:id ORDER BY OrderSort ASC")
    List<PlaylistItem> getCurrentListWithID(int id);
}
