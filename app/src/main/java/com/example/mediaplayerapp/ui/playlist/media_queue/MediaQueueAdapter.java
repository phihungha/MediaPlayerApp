package com.example.mediaplayerapp.ui.playlist.media_queue;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueue;
import com.example.mediaplayerapp.data.playlist.media_queue.MediaQueueViewModel;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.ui.playlist.playlist_details.ItemTouchHelperAdapter;
import com.example.mediaplayerapp.ui.playlist.playlist_details.MediaUtils;
import com.example.mediaplayerapp.ui.playlist.playlist_details.OnPlaylistItemListChangedListener;
import com.example.mediaplayerapp.ui.playlist.playlist_details.OnStartDragListener;

import java.util.ArrayList;

public class MediaQueueAdapter extends ListAdapter<MediaQueue,MediaQueueViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private IOnItemClickListener deleteItemListener;
    private IOnItemClickListener itemClickListener;

    private OnStartDragListener mDragStartListener;
    private OnPlaylistItemListChangedListener mListChangedListener;
    private MediaQueueViewModel viewModel;
    private final ArrayList<Integer> listPos;

    private DisplayMode displayMode = DisplayMode.LIST;

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
                holder.binding.imgThumbnailMediaQueue.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            mDragStartListener.onStartDrag(holder);
                        }
                        return false;
                    }
                });
            } else{
                holder.setBindingGrid(current);
                holder.gridBinding.imgThumbQueueGrid.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            mDragStartListener.onStartDrag(holder);
                        }
                        return false;
                    }
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

    public void swapItem() {
        int fromPosition = getFirstPos();
        int toPosition = getSecondPos();
        MediaQueue first = getCurrentList().get(fromPosition);
        MediaQueue second = getCurrentList().get(toPosition);
        long order = first.getOrderSort();
        first.setOrderSort(second.getOrderSort());
        second.setOrderSort(order);
        viewModel.update(first);
        viewModel.update(second);
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

    public void setListChangedListener(OnPlaylistItemListChangedListener mListChangedListener) {
        this.mListChangedListener = mListChangedListener;
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
