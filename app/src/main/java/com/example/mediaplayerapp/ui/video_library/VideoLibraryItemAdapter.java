package com.example.mediaplayerapp.ui.video_library;

import android.graphics.Bitmap;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VideoLibraryItemAdapter extends RecyclerView.Adapter<VideoLibraryItemAdapter.ViewHolder> {

    private List<Video> mVideos;

    public VideoLibraryItemAdapter() {
        mVideos = new ArrayList<>();
    }

    public VideoLibraryItemAdapter(List<Video> videos) {
        mVideos = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemVideoLibraryGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mVideoThumbnail.setImageBitmap(mVideos.get(position).thumbNail);
        holder.mVideoName.setText(mVideos.get(position).name);

        int duration = mVideos.get(position).duration;
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

    public void updateVideoList(final List<Video> updatedVideoList) {
        this.mVideos.clear();
        this.mVideos = updatedVideoList;
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
