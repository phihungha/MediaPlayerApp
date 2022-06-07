package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.example.mediaplayerapp.utils.item_touch.ItemTouchHelperAdapter;
import com.example.mediaplayerapp.utils.MediaUtils;
import com.example.mediaplayerapp.utils.item_touch.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaItemAdapter extends ListAdapter<PlaylistItem, MediaItemViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private IOnItemClickListener itemClickListener;
    private IOnItemClickListener bsPlayListener;
    private IOnItemClickListener bsDeleteListener;
    private IOnItemClickListener bsPropertiesListener;
    private IOnItemClickListener bsAddQueueListener;
    private IOnItemClickListener bsAddFavouriteListener;

    private Playlist mPlaylist;
    private OnStartDragListener mDragStartListener;

    private DisplayMode displayMode = DisplayMode.LIST;

    private PlaylistItemViewModel viewModel;
    private final ArrayList<Integer> listPos;

    public MediaItemAdapter(@NonNull DiffUtil.ItemCallback<PlaylistItem> diffCallback) {
        super(diffCallback);
        listPos = new ArrayList<>();
    }

    @NonNull
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaItemViewHolder.create(parent, mContext, itemClickListener,
                bsPlayListener, bsDeleteListener, bsPropertiesListener, bsAddQueueListener, bsAddFavouriteListener,
                mPlaylist, this, displayMode);
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getItemViewType(int position) {
        return displayMode.getValue();
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
            if (displayMode == DisplayMode.LIST) {
                holder.setBindingList(current);
                holder.binding.imgThumbnailPlaylistDetails.setOnTouchListener((view, motionEvent) -> {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return true;
                });
            } else {
                holder.setBindingGrid(current);
                holder.gridBinding.imgThumbPlaylistDetailGrid.setOnTouchListener((view, motionEvent) -> {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return true;
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

        List<PlaylistItem> current = viewModel.getCurrentListWithID(mPlaylist.getId());

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(current, i, i + 1);
            }
        } else if (fromPosition > toPosition) {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(current, i, i - 1);
            }
        }

        for (int i = 0; i < current.size(); i++) {
            PlaylistItem item = current.get(i);
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

    public void clearList() {
        listPos.clear();
    }

    public int getSecondPos() {
        return listPos.get(listPos.size() - 1);
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d("TAG", "DISMISS");
    }

    @Override
    public void submitList(@Nullable List<PlaylistItem> list) {
        super.submitList(list);
    }

    public static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistItem> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri());
        }
    }

    public PlaylistItem getPlaylistMediaItemAt(int position) {
        return getItem(position);
    }

    public void setDragStartListener(OnStartDragListener mDragStartListener) {
        this.mDragStartListener = mDragStartListener;
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

    public void setViewModel(PlaylistItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

}
