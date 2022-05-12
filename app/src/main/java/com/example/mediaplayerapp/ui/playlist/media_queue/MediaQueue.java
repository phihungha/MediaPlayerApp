package com.example.mediaplayerapp.ui.playlist.media_queue;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mediaQueue_table")
public class MediaQueue {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MediaQueueId")
    private int id;

    @ColumnInfo(name = "MediaQueueUri")
    private String mediaUri;

    @ColumnInfo(name = "MediaQueueName")
    private String name;

    public MediaQueue(int id, String mediaUri, String name) {
        this.id = id;
        this.mediaUri = mediaUri;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
/*private static List<PlaylistMedia> mListMedia;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public void setContext(Context mContext) {
        MediaQueue.mContext = mContext;
    }

    public static void addToQueue(PlaylistMedia media) {
        mListMedia.add(media);
        makeToast("Media added to queue!!!");
    }

    public static void playQueue(){
        List<Uri> uriMedia=new ArrayList<>();
        mListMedia.forEach(item ->{
            uriMedia.add(Uri.parse(item.getMediaUri()));
        });

        /**
 *
 *      Play media with uri
 *
 * *//*
        // if media is done -> deleteFirst()
    }

    public static void deleteFirst(){
        if (mListMedia!=null){
            mListMedia.remove(0);
        }
    }

    public static void clearQueue(){
        mListMedia.clear();
        makeToast("Queue deleted!!!");
    }

    private static void makeToast(String mess) {
        Toast.makeText(mContext, mess, Toast.LENGTH_SHORT).show();
    }

    public MediaQueue() {
        mListMedia = new ArrayList<>();
    }*/