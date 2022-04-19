package com.example.mediaplayerapp.ui.video_library;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryGridBinding;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VideoLibraryItemAdapter extends RecyclerView.Adapter<VideoLibraryItemAdapter.ViewHolder> {

    private final List<Video> mVideos;

    public VideoLibraryItemAdapter() {
        mVideos = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemVideoLibraryGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mVideoThumbnail.setImageBitmap(mVideos.get(position).getThumbNail());

        holder.mVideoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> videoUris = new ArrayList<>();
                videoUris.add(mVideos.get(position).getUri().toString());

                Intent startPlaybackIntent = new Intent(
                        view.getContext(), VideoPlayerActivity.class);
                startPlaybackIntent.putStringArrayListExtra(
                        VideoPlayerActivity.VIDEO_URI_LIST, videoUris);

                view.getContext().startActivity(startPlaybackIntent);
            }
        });

        holder.mVideoName.setText(mVideos.get(position).getName());

        int duration = mVideos.get(position).getDuration();
        String durationFormatted = String.format(
                Locale.US,
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
        );
        holder.mVideoDuration.setText(durationFormatted);

        holder.mVideoOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);

                TextView videoNameTextview =
                        bottomSheetDialog.findViewById(R.id.video_name_bottom_sheet_textview);
                videoNameTextview.setText(holder.mVideoName.getText());

                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void updateVideoList(final List<Video> updatedVideoList, VideoLibraryFragment.SortArgs sortArgs, VideoLibraryFragment.SortOrder sortOrder) {

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
        this.mVideos.clear();
        this.mVideos.addAll(updatedVideoList);
        //notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ShapeableImageView mVideoThumbnail;
        public final TextView mVideoName;
        public final TextView mVideoDuration;
        public final ImageView mVideoOptions;

        public ViewHolder(ItemVideoLibraryGridBinding binding) {
            super(binding.getRoot());
            mVideoThumbnail = binding.videoThumbnailShapeableimageview;
            mVideoName = binding.videoNameTextview;
            mVideoDuration = binding.videoDurationTextview;
            mVideoOptions = binding.videoOptionsImageview;
        }
    }
}
