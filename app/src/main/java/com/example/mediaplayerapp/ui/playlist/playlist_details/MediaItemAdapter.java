package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MediaItemAdapter extends ListAdapter<PlaylistItem, MediaItemViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private IOnItemClickListener itemClickListener;
    private IOnItemClickListener bsPlayListener;
    private IOnItemClickListener bsDeleteListener;
    private IOnItemClickListener bsPropertiesListener;
    private IOnItemClickListener bsAddQueueListener;
    private IOnItemClickListener bsAddFavouriteListener;
    private Application mApplication;
    private Playlist mPlaylist;
    private OnStartDragListener mDragStartListener;
    private OnPlaylistItemListChangedListener mListChangedListener;
    private PlaylistItemViewModel viewModel;

    private final ArrayList<Integer> listPos;

    public void setViewModel(PlaylistItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setDragStartListener(OnStartDragListener mDragStartListener) {
        this.mDragStartListener = mDragStartListener;
    }

    public void setListChangedListener(OnPlaylistItemListChangedListener mListChangedListener) {
        this.mListChangedListener = mListChangedListener;
    }

    public void setPlaylist(Playlist mPlaylist) {
        this.mPlaylist = mPlaylist;
    }

    public void setBsAddQueueListener(IOnItemClickListener bsAddQueueListener) {
        this.bsAddQueueListener = bsAddQueueListener;
    }

    public void setBsAddFavouriteListener(IOnItemClickListener bsAddFavouriteListener) {
        this.bsAddFavouriteListener = bsAddFavouriteListener;
    }

    public void setApplication(Application mApplication) {
        this.mApplication = mApplication;
    }

    public MediaItemAdapter(@NonNull DiffUtil.ItemCallback<PlaylistItem> diffCallback) {
        super(diffCallback);
        listPos = new ArrayList<>();
    }

    public void setBsPropertiesListener(IOnItemClickListener bsPropertiesListener) {
        this.bsPropertiesListener = bsPropertiesListener;
    }

    public void setItemClickListener(IOnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setBsPlayListener(IOnItemClickListener bsPlayListener) {
        this.bsPlayListener = bsPlayListener;
    }

    public void setBsDeleteListener(IOnItemClickListener bsDeleteListener) {
        this.bsDeleteListener = bsDeleteListener;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaItemViewHolder.create(parent, mContext, itemClickListener,
                bsPlayListener, bsDeleteListener, bsPropertiesListener, bsAddQueueListener, bsAddFavouriteListener,
                mPlaylist, this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        PlaylistItem current = getPlaylistMediaItemAt(position);
        if (current == null) {
            return;
        }
        Uri uri = Uri.parse(current.getMediaUri());
        if (MediaUtils.isUriExists(mContext, uri)) {
            holder.setBinding(current);
            holder.binding.imgThumbnailPlaylistDetails.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        } else {
            PlaylistItemViewModel playlistItemViewModel = new PlaylistItemViewModel(mApplication);
            playlistItemViewModel.deleteItemWithUri(uri.toString());
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        ArrayList<PlaylistItem> currentList = new ArrayList<>(getCurrentList());

        Log.d("TAG_FROM", String.valueOf(fromPosition));
        Log.d("TAG_TO", String.valueOf(toPosition));

        listPos.add(fromPosition);
        listPos.add(toPosition);

        //Collections.swap(currentList,fromPosition,toPosition);
        //mListChangedListener.onNoteListChanged(currentList);
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
        PlaylistItem first = getCurrentList().get(fromPosition);
        PlaylistItem second = getCurrentList().get(toPosition);
        int order = first.getOrderSort();
        first.setOrderSort(second.getOrderSort());
        second.setOrderSort(order);
        viewModel.update(first);
        viewModel.update(second);
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d("TAG", "DISMISS");
    }

    public static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistItem> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri())
                    && oldItem.getId() == newItem.getId();
        }
    };

    public PlaylistItem getPlaylistMediaItemAt(int position) {
        return getItem(position);
    }
}
