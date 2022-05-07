package com.example.mediaplayerapp.ui.video_library;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.video.Video;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryGridBinding;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryListBinding;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VideoLibraryItemAdapter extends RecyclerView.Adapter<VideoLibraryItemAdapter.ViewHolder> {

    private final List<Video> videos;

    public VideoLibraryItemAdapter() {
        videos = new ArrayList<>();
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
                .load(videos.get(position).getUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(holder.videoThumbnail.getWidth(),holder.videoThumbnail.getHeight())
                .centerCrop()
                .into(holder.videoThumbnail);

        holder.videoThumbnail.setOnClickListener(view -> {
            ArrayList<String> videoUris = new ArrayList<>();
            videoUris.add(videos.get(position).getUri().toString());

            Intent startPlaybackIntent = new Intent(
                    view.getContext(), VideoPlayerActivity.class);
            startPlaybackIntent.putStringArrayListExtra(
                    VideoPlayerActivity.VIDEO_URI_LIST, videoUris);

            view.getContext().startActivity(startPlaybackIntent);
        });

        holder.videoName.setText(videos.get(position).getName());

        int duration = videos.get(position).getDuration();
        String durationFormatted = String.format(
                Locale.US,
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
        );
        holder.videoDuration.setText(durationFormatted);

        holder.videoOptions.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
            bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);

            TextView videoNameTextview =
                    bottomSheetDialog.findViewById(R.id.video_name_bottom_sheet_textview);

            if (videoNameTextview != null) {
                videoNameTextview.setText(holder.videoName.getText());
            }

            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

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
        this.videos.clear();
        this.videos.addAll(updatedVideoList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ShapeableImageView videoThumbnail;
        public final TextView videoName;
        public final TextView videoDuration;
        public final ImageView videoOptions;

        public ViewHolder(ItemVideoLibraryListBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoName = binding.videoNameTextview;
            videoDuration = binding.videoDurationTextview;
            videoOptions = binding.videoOptionsImageview;
        }
        public ViewHolder(ItemVideoLibraryGridBinding binding) {
            super(binding.getRoot());
            videoThumbnail = binding.videoThumbnailShapeableimageview;
            videoName = binding.videoNameTextview;
            videoDuration = binding.videoDurationTextview;
            videoOptions = binding.videoOptionsImageview;
        }
    }
}
