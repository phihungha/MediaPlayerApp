package com.example.mediaplayerapp.data.playlist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaylistItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(PlaylistItem item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<PlaylistItem> items);

    @Update
    Completable update(PlaylistItem item);

    @Update
    Completable update(List<PlaylistItem> items);

    @Delete
    Completable delete(PlaylistItem item);

    @Query("DELETE FROM PlaylistItems WHERE PlaylistItemId = :id")
    Completable delete(int id);

    @Query("SELECT * FROM PlaylistItems WHERE PlaylistId = :playlistId ORDER BY OrderIndex ASC")
    Flowable<List<PlaylistItem>> getAllOfPlaylist(int playlistId);

    @Query("SELECT COUNT(*) FROM PlaylistItems WHERE PlaylistId = :playlistId")
    Single<Integer> getItemCountOfPlaylist(int playlistId);

    @Query("SELECT * FROM PlaylistItems WHERE PlaylistId = :playlistId ORDER BY OrderIndex ASC")
    Flowable<PlaylistItem> getFirstItemOfPlaylist(int playlistId);

    @Query("DELETE FROM PlaylistItems WHERE PlaylistId = :playlistId")
    Completable deleteAllFromPlaylist(int playlistId);
}
