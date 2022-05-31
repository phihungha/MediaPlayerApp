package com.example.mediaplayerapp.ui.overview;

import android.content.Context;
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
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.example.mediaplayerapp.utils.MediaThumbnailUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OverviewItemAdapter
        extends ListAdapter<MediaPlaybackInfo, OverviewItemAdapter.ViewHolder> {

    private final List<MediaPlaybackInfo> mediaPlaybackInfoList;
    private final OverviewFragment.MediaType mediaType;
    private final OverviewFragment.MediaLayoutType mediaLayoutType;
    private Context context;

    protected OverviewItemAdapter(@NonNull DiffUtil.ItemCallback<MediaPlaybackInfo> diffCallback,
                                  OverviewFragment.MediaType mediaType,
                                  OverviewFragment.MediaLayoutType mediaLayoutType,
                                  Context context) {
        super(diffCallback);
        mediaPlaybackInfoList = new ArrayList<>();
        this.mediaType = mediaType;
        this.mediaLayoutType = mediaLayoutType;
        this.context = context;
    }

    @NonNull
    @Override
    public OverviewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            Glide.with(holder.mediaThumbnail.getContext())
                    .load(Uri.parse(mediaPlaybackInfoList.get(position).getMediaUri()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_play_video_24dp)
                    .override(holder.mediaThumbnail.getWidth(), holder.mediaThumbnail.getHeight())
                    .centerCrop()
                    .into(holder.mediaThumbnail);
        } else try {
            // Somehow, glide doesn't work for songs' cover thumbnail
            Bitmap thumbnail = MediaThumbnailUtils.getThumbnailFromUri(
                    holder.mediaThumbnail.getContext(),
                    Uri.parse(mediaPlaybackInfoList.get(position).getMediaUri()));
            holder.mediaThumbnail.setImageBitmap(thumbnail);
        } catch (IOException e) {
            holder.mediaThumbnail.setImageDrawable(
                    ContextCompat.getDrawable(holder.mediaThumbnail.getContext(),
                            R.drawable.default_song_artwork));
        }

        Uri mediaUri = Uri.parse(mediaPlaybackInfoList.get(position).getMediaUri());

        // This method uses MediaMetadataRetriever to get media name
        String mediaName = MediaMetadataUtils.getMediaNameFromUri(context, mediaUri);
        if (mediaName != null) holder.mediaName.setText(mediaName);

        String mediaArtist = MediaMetadataUtils.getMediaArtistFromUri(context, mediaUri);
        if (mediaArtist != null) holder.mediaArtist.setText(mediaArtist);
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
        public final ShapeableImageView mediaThumbnail;
        public final LinearLayout mediaClickArea;
        public final TextView mediaName;
        public final TextView mediaArtist;

        public ViewHolder(ItemOverviewVideoSmallBinding binding) {
            super(binding.getRoot());
            mediaThumbnail = binding.videoThumbnailShapeableimageview;
            mediaClickArea = binding.videoClickAreaLinearlayout;
            mediaName = binding.videoNameTextview;
            mediaArtist = null;
        }

        public ViewHolder(ItemOverviewVideoBigBinding binding) {
            super(binding.getRoot());
            mediaThumbnail = binding.videoThumbnailShapeableimageview;
            mediaClickArea = binding.videoClickAreaLinearlayout;
            mediaName = binding.videoNameTextview;
            mediaArtist = null;
        }

        public ViewHolder(ItemOverviewSongSmallBinding binding) {
            super(binding.getRoot());
            mediaThumbnail = binding.songThumbnailShapeableimageview;
            mediaClickArea = binding.songClickAreaLinearlayout;
            mediaName = binding.songNameTextview;
            mediaArtist = binding.songArtistTextview;
        }

        public ViewHolder(ItemOverviewSongBigBinding binding) {
            super(binding.getRoot());
            mediaThumbnail = binding.songThumbnailShapeableimageview;
            mediaClickArea = binding.songClickAreaLinearlayout;
            mediaName = binding.songNameTextview;
            mediaArtist = binding.songArtistTextview;
        }
    }
}
