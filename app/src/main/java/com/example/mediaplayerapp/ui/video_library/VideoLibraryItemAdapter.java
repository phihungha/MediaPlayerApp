package com.example.mediaplayerapp.ui.video_library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryGridBinding;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryListBinding;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VideoLibraryItemAdapter
        extends RecyclerView.Adapter<VideoLibraryItemAdapter.ViewHolder> implements Filterable {

    private final List<Video> displayedVideos;
    private final List<Video> allVideos;
    private final Context context;

    public VideoLibraryItemAdapter(Context context) {
        this.context = context;
        displayedVideos = new ArrayList<>();
        allVideos = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (VideoLibraryFragment.recyclerViewColumnCount > 1)
            return new ViewHolder(ItemVideoLibraryGridBinding.inflate
                    (LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ViewHolder(ItemVideoLibraryListBinding.inflate
                    (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide
                .with(holder.videoThumbnail.getContext())
                .load(displayedVideos.get(position).getUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(holder.videoThumbnail.getWidth(), holder.videoThumbnail.getHeight())
                .centerCrop()
                .into(holder.videoThumbnail);

        holder.videoClickArea.setOnClickListener(view -> {
            ArrayList<String> videoUris = new ArrayList<>();
            videoUris.add(displayedVideos.get(position).getUri().toString());

            Intent startPlaybackIntent = new Intent(
                    view.getContext(), VideoPlayerActivity.class);
            startPlaybackIntent.putStringArrayListExtra(
                    VideoPlayerActivity.VIDEO_URI_LIST, videoUris);

            view.getContext().startActivity(startPlaybackIntent);
        });

        holder.videoName.setText(displayedVideos.get(position).getName());

        int duration = displayedVideos.get(position).getDuration();
        String durationFormatted = String.format(
                Locale.US,
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
        );
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

    @SuppressLint("NotifyDataSetChanged")
    public void updateVideoList(
            final List<Video> updatedVideoList
            , VideoLibraryFragment.SortArgs sortArgs
            , VideoLibraryFragment.SortOrder sortOrder) {

        switch (sortArgs) {
            case VIDEO_NAME:
                if (sortOrder == VideoLibraryFragment.SortOrder.ASC)
                    updatedVideoList.sort(Video.VideoNameAZComparator);
                else
                    updatedVideoList.sort(Video.VideoNameZAComparator);
                break;

            case VIDEO_DURATION:
                if (sortOrder == VideoLibraryFragment.SortOrder.ASC)
                    updatedVideoList.sort(Video.VideoDurationAscendingComparator);
                else
                    updatedVideoList.sort(Video.VideoDurationDescendingComparator);
                break;

            default:
                break;
        }
        this.displayedVideos.clear();
        this.displayedVideos.addAll(updatedVideoList);

        this.allVideos.clear();
        this.allVideos.addAll(updatedVideoList);
        notifyDataSetChanged();
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

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedVideos.clear();

                // Using Android refactoring with Alt + Enter doesn't resolve this warning
                displayedVideos.addAll((List<Video>) filterResults.values);
                notifyDataSetChanged();
            }
        };
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
