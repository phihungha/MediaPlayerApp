package com.example.mediaplayerapp.ui.video_library;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.databinding.ItemVideoLibraryGridBinding;
import com.google.android.material.imageview.ShapeableImageView;

public class VideoLibraryItemAdapter extends RecyclerView.Adapter<VideoLibraryItemAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemVideoLibraryGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mVideoName.setText("Video-name-" + position);
        holder.mVideoDuration.setText("Video-duration-" + position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ShapeableImageView mVideoThumbnail;
        public final TextView mVideoName;
        public final TextView mVideoDuration;

        public ViewHolder(ItemVideoLibraryGridBinding binding) {
            super(binding.getRoot());
            mVideoThumbnail = binding.videoThumbnailShapeableimageview;
            mVideoName = binding.videoNameTextview;
            mVideoDuration = binding.videoDurationTextview;
        }
    }
}
