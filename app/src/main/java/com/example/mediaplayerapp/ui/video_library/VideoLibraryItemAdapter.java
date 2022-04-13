package com.example.mediaplayerapp.ui.video_library;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.data.Video;
import com.example.mediaplayerapp.databinding.ItemVideoLibraryGridBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

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
        holder.mVideoDuration.setText(String.valueOf(mVideos.get(position).duration));
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

        public ViewHolder(ItemVideoLibraryGridBinding binding) {
            super(binding.getRoot());
            mVideoThumbnail = binding.videoThumbnailShapeableimageview;
            mVideoName = binding.videoNameTextview;
            mVideoDuration = binding.videoDurationTextview;
        }
    }
}
