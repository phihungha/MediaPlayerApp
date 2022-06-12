package com.example.mediaplayerapp.ui.special_playlists;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.special_playlists.MediaQueue;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.item_touch_helper.ItemTouchHelperAdapter;
import com.example.mediaplayerapp.utils.item_touch_helper.ItemTouchHelperViewHolder;
import com.example.mediaplayerapp.utils.item_touch_helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaQueueAdapter extends ListAdapter<MediaQueue,MediaQueueAdapter.MediaQueueViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private IOnItemClickListener deleteItemListener;
    private IOnItemClickListener itemClickListener;

    private MediaQueueViewModel viewModel;
    private final ArrayList<Integer> listPos;
    private int type;

    private DisplayMode displayMode = DisplayMode.LIST;

    public void setType(int type) {
        this.type = type;
    }

    public MediaQueueAdapter(@NonNull DiffUtil.ItemCallback<MediaQueue> diffCallback) {
        super(diffCallback);
        listPos = new ArrayList<>();
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
        View itemView;
        if (displayMode == DisplayMode.GRID) {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_media_queue_grid,
                    parent,
                    false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_media_queue_list,
                    parent,
                    false);
        }
        return new MediaQueueViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MediaQueueViewHolder holder, int position) {
        MediaQueue current = getItemAt(position);
        if (current == null) {
            return;
        }
        holder.setBinding(current);
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
        switch (type) {
            case PlaylistConstants
                    .TYPE_VIDEO_QUEUE:
                current = viewModel.getCurrentListVideoWatchLater();
                break;

            case PlaylistConstants
                    .TYPE_MUSIC_QUEUE:
                current = viewModel.getCurrentListSongWatchLater();
                break;

            case PlaylistConstants
                    .TYPE_VIDEO_FAVOURITE:
                current = viewModel.getCurrentListVideoFavourite();
                break;

            case PlaylistConstants
                    .TYPE_MUSIC_FAVOURITE:
                current = viewModel.getCurrentListSongFavourite();
                break;
        }

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
            MediaQueue item = current.get(i);
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

    public MediaQueue getItemAt(int position) {
        return getItem(position);
    }

    public void setItemClickListener(IOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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

    public class MediaQueueViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {
        private final TextView tvName;
        private final ImageView imgThumb;
        private final TextView tvDuration;

        public MediaQueueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_queue_name);
            imgThumb=itemView.findViewById(R.id.img_thumb_queue);
            tvDuration=itemView.findViewById(R.id.tv_duration_queue);

            ImageButton btnDelete = itemView.findViewById(R.id.imgBtn_queue_delete);
            btnDelete.setOnClickListener(view -> DeleteItemQueue());

            itemView.setOnClickListener(view -> QueueItemClick());
        }

        public void setBinding(MediaQueue media) {
            String name= MediaUtils.getMediaNameFromURI(mContext, Uri.parse(media.getMediaUri()));
            tvName.setText(name);

            MediaInfo mediaInfo = MediaUtils.getInfoWithUri(mContext, Uri.parse(media.getMediaUri()));
            String duration = MediaTimeUtils.getFormattedTimeFromLong(mediaInfo.getDuration());
            tvDuration.setText(duration);

            setItemThumbnail();
        }
        private void setItemThumbnail() {
            Drawable thumbnail = MediaMetadataUtils.getThumbnail(
                    mContext,
                    getItemAt(getBindingAdapterPosition()).getAndroidMediaUri(),
                    R.drawable.ic_playlist_24dp);
            imgThumb.setImageDrawable(thumbnail);
        }

        private void QueueItemClick() {
            if (itemClickListener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                itemClickListener.onClick(itemView.getRootView(), getBindingAdapterPosition());
            }
        }

        private void DeleteItemQueue() {
            if (deleteItemListener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                deleteItemListener.onClick(itemView.getRootView(), getBindingAdapterPosition());
            }
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundResource(R.color.bright_grey);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);

            if (getListPosSize() < 2)
                return;

            swapItem();
            clearListPos();
        }
    }
}