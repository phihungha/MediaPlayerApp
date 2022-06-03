package com.example.mediaplayerapp.ui.video_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.video_library.Video;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryGridBinding;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryListBinding;
import com.example.mediaplayerapp.ui.DisplayMode;
import com.example.mediaplayerapp.utils.MediaTimeUtils;
import com.example.mediaplayerapp.utils.StartPlaybackCallback;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class VideoAdapter
        extends ListAdapter<Video, VideoAdapter.ViewHolder>
        implements Filterable {

    private DisplayMode displayMode = DisplayMode.LIST;
    private final Context context;

    private final StartPlaybackCallback startPlaybackCallback;
    private List<Video> allVideos = new ArrayList<>();
    private final List<Video> displayedVideos = new ArrayList<>();

    protected VideoAdapter(Context context, StartPlaybackCallback startPlaybackCallback) {
        super(new VideoDiff());
        this.context = context;
        this.startPlaybackCallback = startPlaybackCallback;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return displayMode.getValue();
    }

    @Override
    public void submitList(@Nullable List<Video> newVideos) {
        assert newVideos != null;
        super.submitList(new ArrayList<>(newVideos));
        allVideos = newVideos;
        displayedVideos.clear();
        displayedVideos.addAll(newVideos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (displayMode == DisplayMode.GRID)
            return new ViewHolder(ItemVideoLibraryGridBinding.inflate
                    (LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ViewHolder(ItemVideoLibraryListBinding.inflate
                    (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.videoThumbnail.getContext())
            .load(displayedVideos.get(position).getUri())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_video_24dp)
            .override(holder.videoThumbnail.getWidth(), holder.videoThumbnail.getHeight())
            .centerCrop()
            .into(holder.videoThumbnail);

        holder.videoClickArea.setOnClickListener(view -> startPlaybackCallback.play(position));

        holder.videoName.setText(displayedVideos.get(position).getName());

        int duration = displayedVideos.get(position).getDuration();
        String durationFormatted = MediaTimeUtils.getFormattedTimeFromLong(duration);
        holder.videoDuration.setText(durationFormatted);

        holder.videoOptions.setOnClickListener(view -> {

            VideoLibraryBottomSheetDialog bottomSheetDialog =
                    new VideoLibraryBottomSheetDialog(displayedVideos.get(position));

            bottomSheetDialog.show(((AppCompatActivity) context).getSupportFragmentManager(),
                    bottomSheetDialog.getTag());

        });
    }

    @Override
    public int getItemCount() {
        return displayedVideos.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<Video> filteredVideos = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredVideos.addAll(allVideos);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (Video video : allVideos) {
                        if (video.getName().toLowerCase().contains(filterPattern))
                            filteredVideos.add(video);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredVideos;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedVideos.clear();

                // Java cannot ensure safe casting of generic types
                //noinspection unchecked
                displayedVideos.addAll((List<Video>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    static class VideoDiff extends DiffUtil.ItemCallback<Video> {
        @Override
        public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem.getUri().equals(newItem.getUri());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ShapeableImageView videoThumbnail;
        public final LinearLayout videoClickArea;
        public final TextView videoName;
        public final TextView videoDuration;
        public final ImageView videoOptions;

        public ViewHolder(ItemVideoLibraryListBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoClickArea = binding.videoClickAreaLinearlayout;
            videoName = binding.videoNameTextview;
            videoDuration = binding.videoDurationTextview;
            videoOptions = binding.videoOptionsImageview;
        }

        public ViewHolder(ItemVideoLibraryGridBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoClickArea = binding.videoClickAreaLinearlayout;
            videoName = binding.videoNameTextview;
            videoDuration = binding.videoDurationTextview;
            videoOptions = binding.videoOptionsImageview;
        }
    }
}
