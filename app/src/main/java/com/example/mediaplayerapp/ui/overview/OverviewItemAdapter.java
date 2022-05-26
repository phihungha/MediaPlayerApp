package com.example.mediaplayerapp.ui.overview;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.overview.MediaPlaybackInfo;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoSmallBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class OverviewItemAdapter
        extends ListAdapter<MediaPlaybackInfo,OverviewItemAdapter.ViewHolder> {

    private final List<MediaPlaybackInfo> mediaPlaybackInfoList;

    protected OverviewItemAdapter(@NonNull DiffUtil.ItemCallback<MediaPlaybackInfo> diffCallback) {
        super(diffCallback);
        mediaPlaybackInfoList = new ArrayList<>();
    }

    static class MediaPlaybackInfoDiff extends DiffUtil.ItemCallback<MediaPlaybackInfo> {

        @Override
        public boolean areItemsTheSame(@NonNull MediaPlaybackInfo oldItem,
                                       @NonNull MediaPlaybackInfo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MediaPlaybackInfo oldItem,
                                          @NonNull MediaPlaybackInfo newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri());
        }
    }
    @NonNull
    @Override
    public OverviewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOverviewVideoSmallBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewItemAdapter.ViewHolder holder, int position) {
        Glide.with(holder.videoThumbnail.getContext())
                .load(mediaPlaybackInfoList.get(position).getMediaUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_video_24dp)
                .override(holder.videoThumbnail.getWidth(), holder.videoThumbnail.getHeight())
                .centerCrop()
                .into(holder.videoThumbnail);
        holder.videoName.setText(mediaPlaybackInfoList.get(position).getMediaUri());
    }

    @Override
    public int getItemCount() {
        return mediaPlaybackInfoList.size();
    }

    @Override
    public void submitList(@Nullable List<MediaPlaybackInfo> list) {
        assert list != null;
        super.submitList(new ArrayList<>(list));
        this.mediaPlaybackInfoList.clear();
        this.mediaPlaybackInfoList.addAll(list);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final ShapeableImageView videoThumbnail;
        public final LinearLayout videoClickArea;
        public final TextView videoName;

        public ViewHolder(ItemOverviewVideoSmallBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoClickArea = binding.videoClickAreaLinearlayout;
            videoName = binding.videoNameTextview;
        }
    }
}
