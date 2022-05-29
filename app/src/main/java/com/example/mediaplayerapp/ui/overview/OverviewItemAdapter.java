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
import com.example.mediaplayerapp.databinding.ItemOverviewVideoBigBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoSmallBinding;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OverviewItemAdapter
        extends ListAdapter<MediaPlaybackInfo, OverviewItemAdapter.ViewHolder> {

    private final List<MediaPlaybackInfo> mediaPlaybackInfoList;

    protected OverviewItemAdapter(@NonNull DiffUtil.ItemCallback<MediaPlaybackInfo> diffCallback) {
        super(diffCallback);
        mediaPlaybackInfoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public OverviewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(ItemOverviewVideoSmallBinding.inflate
//                (LayoutInflater.from(parent.getContext()), parent, false));

        return new ViewHolder(ItemOverviewVideoBigBinding.inflate
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

        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy - hh:mm a", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mediaPlaybackInfoList.get(position).getLastPlaybackTime());
        holder.videoPlaybackTime.setText(formatter.format(calendar.getTime()));

        String lastPlaybackPosi = MediaTimeUtils.getFormattedTime(mediaPlaybackInfoList.get(position).getLastPlaybackPosition());
        holder.videoPlaybackPosi.setText(lastPlaybackPosi);

        holder.videoPlaybackAmount.setText(String.valueOf(mediaPlaybackInfoList.get(position).getPlaybackAmount()));

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ShapeableImageView videoThumbnail;
        public final LinearLayout videoClickArea;
        public final TextView videoName;
        public final TextView videoPlaybackTime;
        public final TextView videoPlaybackPosi;
        public final TextView videoPlaybackAmount;

        public ViewHolder(ItemOverviewVideoSmallBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoClickArea = binding.videoClickAreaLinearlayout;
            videoName = binding.videoNameTextview;
            videoPlaybackTime = binding.videoLastPlaybackTextview;
            videoPlaybackPosi = binding.videoPlaybackPositionTextview;
            videoPlaybackAmount = binding.videoPlaybackAmountTextview;
        }

        public ViewHolder(ItemOverviewVideoBigBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoClickArea = binding.videoClickAreaLinearlayout;
            videoName = binding.videoNameTextview;
            videoPlaybackTime = binding.videoLastPlaybackTextview;
            videoPlaybackPosi = binding.videoPlaybackPositionTextview;
            videoPlaybackAmount = binding.videoPlaybackAmountTextview;
        }
    }
}
