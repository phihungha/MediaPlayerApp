package com.example.mediaplayerapp.ui.overview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.mediaplayerapp.data.playback_history.PlaybackHistoryEntry;
import com.example.mediaplayerapp.databinding.ItemOverviewSongBigBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewSongSmallBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoBigBinding;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoSmallBinding;
import com.example.mediaplayerapp.ui.music_player.MusicPlayerActivity;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.example.mediaplayerapp.utils.MediaMetadataUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class OverviewItemAdapter
        extends ListAdapter<PlaybackHistoryEntry, OverviewItemAdapter.ViewHolder> {

    private final List<PlaybackHistoryEntry> playbackHistoryEntryList;
    private final OverviewFragment.MediaType mediaType;
    private final OverviewFragment.MediaLayoutType mediaLayoutType;
    private final Context context;

    protected OverviewItemAdapter(@NonNull DiffUtil.ItemCallback<PlaybackHistoryEntry> diffCallback,
                                  OverviewFragment.MediaType mediaType,
                                  OverviewFragment.MediaLayoutType mediaLayoutType,
                                  Context context) {
        super(diffCallback);
        playbackHistoryEntryList = new ArrayList<>();
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
                    .load(Uri.parse(playbackHistoryEntryList.get(position).getMediaUri()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_play_video_24dp)
                    .override(holder.mediaThumbnail.getWidth(), holder.mediaThumbnail.getHeight())
                    .centerCrop()
                    .into(holder.mediaThumbnail);
        } else {
            // Somehow, glide doesn't work for songs' cover thumbnail
            Drawable thumbnail = MediaMetadataUtils.getThumbnail(
                    holder.mediaThumbnail.getContext(),
                    Uri.parse(playbackHistoryEntryList.get(position).getMediaUri()),
                    R.drawable.default_song_artwork);
            holder.mediaThumbnail.setImageDrawable(thumbnail);
        }

        PlaybackHistoryEntry playbackHistoryEntry = playbackHistoryEntryList.get(position);
        Uri mediaUri = Uri.parse(playbackHistoryEntry.getMediaUri());
        long lastPlaybackPosition = playbackHistoryEntry.getLastPlaybackPosition();

        String mediaName = MediaMetadataUtils.getDisplayName(context, mediaUri);
        if (mediaName != null) holder.mediaName.setText(mediaName);

        if (mediaType == OverviewFragment.MediaType.SONG) {
            String mediaArtist = MediaMetadataUtils.getArtistName(context, mediaUri);
            if (mediaArtist != null)
                holder.mediaArtist.setText(mediaArtist);
        }

        holder.mediaClickArea.setOnClickListener(view -> {
            if (mediaType == OverviewFragment.MediaType.VIDEO)
                VideoPlayerActivity.launchWithUriAndSeekTo(context, mediaUri, lastPlaybackPosition);
            else if (mediaType == OverviewFragment.MediaType.SONG)
                MusicPlayerActivity.launchWithUriAndSeekTo(context, mediaUri, lastPlaybackPosition);
        });
    }

    @Override
    public int getItemCount() {
        return playbackHistoryEntryList.size();
    }

    @Override
    public void submitList(@Nullable List<PlaybackHistoryEntry> list) {
        assert list != null;
        super.submitList(new ArrayList<>(list));
        this.playbackHistoryEntryList.clear();
        this.playbackHistoryEntryList.addAll(list);
    }

    static class MediaPlaybackInfoDiff extends DiffUtil.ItemCallback<PlaybackHistoryEntry> {

        @Override
        public boolean areItemsTheSame(@NonNull PlaybackHistoryEntry oldItem,
                                       @NonNull PlaybackHistoryEntry newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlaybackHistoryEntry oldItem,
                                          @NonNull PlaybackHistoryEntry newItem) {
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
