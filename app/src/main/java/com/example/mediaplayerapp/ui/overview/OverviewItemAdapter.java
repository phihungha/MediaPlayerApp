package com.example.mediaplayerapp.ui.overview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.overview.MediaPlaybackInfo;
import com.example.mediaplayerapp.databinding.ItemOverviewSongBigBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewSongSmallBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoBigBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoSmallBinding;
import com.example.mediaplayerapp.utils.MediaThumbnailUtils;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OverviewItemAdapter
        extends ListAdapter<MediaPlaybackInfo, OverviewItemAdapter.ViewHolder> {

    private final List<MediaPlaybackInfo> mediaPlaybackInfoList;
    private final OverviewFragment.MediaType mediaType;
    private final OverviewFragment.MediaLayoutType mediaLayoutType;

    protected OverviewItemAdapter(@NonNull DiffUtil.ItemCallback<MediaPlaybackInfo> diffCallback,
                                  OverviewFragment.MediaType mediaType,
                                  OverviewFragment.MediaLayoutType mediaLayoutType) {
        super(diffCallback);
        mediaPlaybackInfoList = new ArrayList<>();
        this.mediaType = mediaType;
        this.mediaLayoutType = mediaLayoutType;
    }

    @NonNull
    @Override
    public OverviewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(ItemOverviewVideoSmallBinding.inflate
//                (LayoutInflater.from(parent.getContext()), parent, false));
        if (mediaType == OverviewFragment.MediaType.VIDEO) {
            if (mediaLayoutType == OverviewFragment.MediaLayoutType.BIG) {
                return new ViewHolder(ItemOverviewVideoBigBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false));
            } else {
                return new ViewHolder(ItemOverviewVideoSmallBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false));
            }
        } else {
            if (mediaLayoutType == OverviewFragment.MediaLayoutType.BIG) {
                return new ViewHolder(ItemOverviewSongBigBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false));
            } else {
                return new ViewHolder(ItemOverviewSongSmallBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false));
            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull OverviewItemAdapter.ViewHolder holder, int position) {
        if (mediaType == OverviewFragment.MediaType.VIDEO) {
            Glide.with(holder.videoThumbnail.getContext())
                    .load(Uri.parse(mediaPlaybackInfoList.get(position).getMediaUri()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_play_video_24dp)
                    .override(holder.videoThumbnail.getWidth(), holder.videoThumbnail.getHeight())
                    .centerCrop()
                    .into(holder.videoThumbnail);
        } else try {
            // Somehow, glide doesn't work for songs' cover thumbnail
            Bitmap thumbnail = MediaThumbnailUtils.getThumbnailFromUri(
                    holder.videoThumbnail.getContext(),
                    Uri.parse(mediaPlaybackInfoList.get(position).getMediaUri()));
            holder.videoThumbnail.setImageBitmap(thumbnail);
        } catch (IOException e) {
            holder.videoThumbnail.setImageDrawable(
                    ContextCompat.getDrawable(holder.videoThumbnail.getContext(),
                            R.drawable.default_song_artwork));
        }

        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy - hh:mm a", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mediaPlaybackInfoList.get(position).getLastPlaybackTime());
        holder.videoPlaybackTime.setText(formatter.format(calendar.getTime()));
        holder.videoName.setText(mediaPlaybackInfoList.get(position).getMediaUri());
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

        public ViewHolder(ItemOverviewSongSmallBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.songThumbnailShapeableimageview;
            videoClickArea = binding.songClickAreaLinearlayout;
            videoName = binding.songNameTextview;
            videoPlaybackTime = binding.songLastPlaybackTextview;
            videoPlaybackPosi = binding.songPlaybackPositionTextview;
            videoPlaybackAmount = binding.songPlaybackAmountTextview;
        }

        public ViewHolder(ItemOverviewSongBigBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.songThumbnailShapeableimageview;
            videoClickArea = binding.songClickAreaLinearlayout;
            videoName = binding.songNameTextview;
            videoPlaybackTime = binding.songLastPlaybackTextview;
            videoPlaybackPosi = binding.songPlaybackPositionTextview;
            videoPlaybackAmount = binding.songPlaybackAmountTextview;
        }
    }
}
