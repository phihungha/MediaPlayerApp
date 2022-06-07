package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.playlist.PlaylistConstants;
import com.example.mediaplayerapp.utils.IOnItemClickListener;
import com.example.mediaplayerapp.utils.ItemTouchHelperAdapter;
import com.example.mediaplayerapp.utils.MediaUtils;
import com.example.mediaplayerapp.utils.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaQueueAdapter extends ListAdapter<MediaQueue,MediaQueueViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private IOnItemClickListener deleteItemListener;
    private IOnItemClickListener itemClickListener;

    private OnStartDragListener mDragStartListener;
    private MediaQueueViewModel viewModel;
    private final ArrayList<Integer> listPos;
    private int type;

    private DisplayMode displayMode = DisplayMode.LIST;

    public void setType(int type) {
        this.type = type;
    }

    public MediaQueueAdapter(@NonNull DiffUtil.ItemCallback<MediaQueue> diffCallback) {
        super(diffCallback);
        listPos=new ArrayList<>();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getItemViewType(int position) {
        return displayMode.getValue();
    }

    @NonNull
    @Override
    public MediaQueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaQueueViewHolder.create(parent,mContext,deleteItemListener,itemClickListener,this,displayMode);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MediaQueueViewHolder holder, int position) {
        MediaQueue current= getItemAt(position);
        if (current==null){
            return;
        }
        Uri uri=Uri.parse(current.getMediaUri());
        if (MediaUtils.isUriExists(mContext,uri)){
            if (displayMode==DisplayMode.LIST){
                holder.setBindingList(current);
                holder.binding.imgThumbnailMediaQueue.setOnTouchListener((view, motionEvent) -> {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                });
            } else{
                holder.setBindingGrid(current);
                holder.gridBinding.imgThumbQueueGrid.setOnTouchListener((view, motionEvent) -> {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                });
            }

        } else {
            viewModel.deleteItemWithUri(uri.toString());
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        listPos.add(fromPosition);
        listPos.add(toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    public void swapItem() {
        int fromPosition = getFirstPos();
        int toPosition = getSecondPos();
        List<MediaQueue> current = new ArrayList<>();
        switch (type){
            case PlaylistConstants
                    .TYPE_VIDEO_QUEUE:
                current=viewModel.getCurrentListVideoWatchLater();
                break;

            case PlaylistConstants
                    .TYPE_MUSIC_QUEUE:
                current=viewModel.getCurrentListSongWatchLater();
                break;

            case PlaylistConstants
                    .TYPE_VIDEO_FAVOURITE:
                current=viewModel.getCurrentListVideoFavourite();
                break;

            case PlaylistConstants
                    .TYPE_MUSIC_FAVOURITE:
                current=viewModel.getCurrentListSongFavourite();
                break;
        }

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(current,i,i+1);
            }
        } else if (fromPosition > toPosition){
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(current,i,i-1);
            }
        }

        for (int i=0;i<current.size();i++){
            MediaQueue item=current.get(i);
            item.setOrderSort(i);
        }
        viewModel.updateByList(current);
    }

    public int getListPosSize() {
        return listPos.size();
    }

    public int getFirstPos() {
        return listPos.get(0);
    }

    public void clearListPos() {
        listPos.clear();
    }

    public int getSecondPos() {
        return listPos.get(listPos.size() - 1);
    }
    @Override
    public void onItemDismiss(int position) {
    }

    public static class MediaQueueDiff extends DiffUtil.ItemCallback<MediaQueue> {
        @Override
        public boolean areItemsTheSame(@NonNull MediaQueue oldItem, @NonNull MediaQueue newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MediaQueue oldItem, @NonNull MediaQueue newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri());
        }
    }

    public MediaQueue getItemAt(int position){
        return getItem(position);
    }

    public void setItemClickListener(IOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setDragStartListener(OnStartDragListener mDragStartListener) {
        this.mDragStartListener = mDragStartListener;
    }

    public void setViewModel(MediaQueueViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setDeleteItemListener(IOnItemClickListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
}
