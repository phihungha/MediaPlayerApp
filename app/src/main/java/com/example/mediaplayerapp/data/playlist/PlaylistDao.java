package com.example.mediaplayerapp.data.playlist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaylistDao {

    @Insert
    Completable insert(Playlist playlist);

    @Update
    Completable update(Playlist playlist);

    @Query("UPDATE Playlists SET Name = :name WHERE PlaylistId = :id")
    Completable updateName(int id, String name);

    @Query("UPDATE Playlists SET ItemCount = :count WHERE PlaylistId = :id")
    Completable updateItemCount(int id, int count);

    @Delete
    Completable delete(Playlist playlist);

    @Query("DELETE FROM Playlists WHERE PlaylistId = :id")
    Completable delete(int id);

    @Query("SELECT * FROM Playlists WHERE Name LIKE '%' || :text || '%'")
    Flowable<List<Playlist>> getByNameMatching(String text);

    @Query("SELECT * FROM Playlists ORDER BY Name ASC")
    Flowable<List<Playlist>> getAllSortedByNameAsc();

    @Query("SELECT * FROM Playlists ORDER BY Name DESC")
    Flowable<List<Playlist>> getAllSortedByNameDesc();

    @Query("SELECT * FROM Playlists ORDER BY ItemCount ASC")
    Flowable<List<Playlist>> getAllSortedByItemCountAsc();

    @Query("SELECT * FROM Playlists ORDER BY ItemCount DESC")
    Flowable<List<Playlist>> getAllSortedByItemCountDesc();

    @Query("SELECT * FROM Playlists WHERE IsVideo = 0")
    Flowable<List<Playlist>> getAllSongPlaylists();

    @Query("SELECT * FROM Playlists WHERE IsVideo = 1")
    Flowable<List<Playlist>> getAllVideoPlaylists();

    @Query("SELECT * from Playlists WHERE PlaylistId = :id")
    Flowable<Playlist> get(int id);
}
