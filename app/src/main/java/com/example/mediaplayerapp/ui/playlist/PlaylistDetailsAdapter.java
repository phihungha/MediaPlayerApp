package com.example.mediaplayerapp.ui.playlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.data.Playlist;
import com.example.mediaplayerapp.databinding.ItemPlaylistBinding;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.video_player.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailsAdapter extends RecyclerView.Adapter<PlaylistDetailsAdapter.PlaylistDetailsViewHolder>{
    private Context mContext;
    private ArrayList<PlaylistVideoModel> mArrayListVideos;
    private Activity mActivity;

    public void setArrayListVideos(ArrayList<PlaylistVideoModel> mArrayListVideos) {
        this.mArrayListVideos = mArrayListVideos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);

        return new PlaylistDetailsViewHolder(binding);
        /*return new PlaylistDetailsViewHolder(ItemPlaylistDetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));*/
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDetailsViewHolder holder, int position) {
        PlaylistVideoModel video = mArrayListVideos.get(position);
        if (video==null){
            return;
        }
       /* Glide.with(mContext)
                .load("file://"+ video.getThumb())
                .skipMemoryCache(false)
                .into(holder.binding.imgThumbnailPlaylistDetails);*/
        holder.binding.tvPlaylistNamePlaylistDetails.setText(video.getName());

        holder.binding.layoutItemPlaylistDetails.setBackgroundColor(Color.parseColor(PlaylistConstants.COLOR_WHITE));
        holder.binding.layoutItemPlaylistDetails.setAlpha(0);

        holder.binding.layoutItemPlaylistDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i =new Intent(mContext, VideoPlayerActivity.class);
                i.putExtra(PlaylistConstants.EXTRA_VIDEO,video.getPath());
                mActivity.startActivity(i);*/

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mArrayListVideos!=null){
            return mArrayListVideos.size();
        }
        return 0;
    }

    public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder{
        private ItemPlaylistDetailsBinding binding;
        public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
