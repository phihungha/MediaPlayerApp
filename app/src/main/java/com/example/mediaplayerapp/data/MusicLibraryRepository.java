package com.example.mediaplayerapp.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MusicLibraryRepository {
    public static class ArtistLoader {


        public List<Artist> getArtiss(Cursor cursor) {

            List<Artist> list = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3)));
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            }

            return list;
        }

        public static Artist getArtis(Context context, long id) {

            return artis(makeCursor(context, "_id=?", new String[]{String.valueOf(id)}));
        }

        private static Artist artis(Cursor cursor) {
            Artist artis = new Artist();
            if (cursor.moveToFirst() && cursor != null) {

                artis = new Artist(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));

            }
            if (cursor != null) {
                cursor.close();
            }
            return artis;
        }

        public List<Artist> artisList(Context context) {
            return getArtiss(makeCursor(context, null, null));
        }

        public static Cursor makeCursor(Context context, String selection, String[] selectionArg) {


            Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{
                    MediaStore.Audio.Artists._ID,//0
                    MediaStore.Audio.Artists.ARTIST,//1
                    MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,//2
                    MediaStore.Audio.Artists.NUMBER_OF_TRACKS,//3
            };
            String sortOrder = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);
            return cursor;
        }
    }

    public static class ArtistSongLoader {

        public static List<Song> getAllArtistSongs(Context context, long artist_id) {

            ArrayList<Song> artistSongList = new ArrayList<Song>();

            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,//0
                    MediaStore.Audio.Media.TITLE,//1
                    MediaStore.Audio.Media.ALBUM_ID,//2
                    MediaStore.Audio.Media.ALBUM,//3
                    MediaStore.Audio.Media.ARTIST,//4
                    MediaStore.Audio.Media.DURATION,//5
                    MediaStore.Audio.Media.TRACK//6

            };

            String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
            String selection = "is_music=1 and title !='' and artist_id=" + artist_id;
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                do {

                    int trackNumber = cursor.getInt(6);
                    while (trackNumber >= 1000) {
                        trackNumber -= 1000;
                    }
                    artistSongList.add(new Song(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getString(3),
                            artist_id, cursor.getString(4), cursor.getInt(5), trackNumber));
                } while (cursor.moveToNext());

                if (cursor != null) {
                    cursor.close();
                }

            }
            return artistSongList;
        }
    }
    public static class AlbumLoader {


        public ArrayList<Album> getAlbums(Cursor cursor) {

            List<Album> list = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(new Album(cursor.getLong(0), cursor.getString(1), cursor.getLong(2),
                            cursor.getString(3), cursor.getInt(4), cursor.getInt(5)));
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            }

            return (ArrayList<Album>) list;
        }

        public Album getAlbum(Context context, long id) {

            return album(makeCursor(context, "_id=?", new String[]{String.valueOf(id)}));
        }

        private Album album(Cursor cursor) {
            Album album = new Album();
            if (cursor.moveToFirst() && cursor != null) {

                album = new Album(cursor.getLong(0), cursor.getString(1), cursor.getLong(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getInt(5));

            }
            if (cursor != null) {
                cursor.close();
            }
            return album;
        }

        public List<Album> albumList(Context context) {
            return getAlbums(makeCursor(context, null, null));
        }

        public  Cursor makeCursor(Context context, String selection, String[] selectionArg) {


            Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{
                    MediaStore.Audio.Albums._ID,//0
                    MediaStore.Audio.Albums.ALBUM,//1
                    MediaStore.Audio.Albums.ARTIST_ID,//2
                    MediaStore.Audio.Albums.ARTIST,//3
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS,//4
                    MediaStore.Audio.Albums.FIRST_YEAR,//5
            };
            String sortOrder = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArg, sortOrder);
            return cursor;
        }
    }
    public static class AlbumSongLoder {

        public static ArrayList<Song> getAllAlbumSongs(Context context, long _id){

            List<Song> albumSongList = new ArrayList<>();

            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{
                    MediaStore.Audio.Media._ID,//0
                    MediaStore.Audio.Media.TITLE,//1
                    MediaStore.Audio.Media.ALBUM,//2
                    MediaStore.Audio.Media.ARTIST_ID,//3
                    MediaStore.Audio.Media.ARTIST,//4
                    MediaStore.Audio.Media.DURATION,//5
                    MediaStore.Audio.Media.TRACK//6

            };

            String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
            String selection = "is_music=1 and title !='' and album_id="+_id;
            Cursor cursor = context.getContentResolver().query(uri,projection,selection,null,sortOrder);

            if (cursor!=null&&cursor.moveToFirst()){
                do {

                    int trackNumber = cursor.getInt(6);
                    while (trackNumber>=1000){
                        trackNumber-=1000;
                    }
                    albumSongList.add(new Song(cursor.getLong(0),cursor.getString(1),_id,cursor.getString(2),
                            cursor.getLong(3),cursor.getString(4),cursor.getInt(5),trackNumber));
                }while (cursor.moveToNext());

                if (cursor!=null) {
                    cursor.close();
                }

            }

            return (ArrayList<Song>) albumSongList;
        }
    }
}



