package com.example.mediaplayerapp.ui.overview;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.data.overview.MediaPlaybackInfo;
import com.example.mediaplayerapp.databinding.ItemOverviewVideoSmallBinding;

import java.util.ArrayList;
import java.util.List;

public class OverviewItemAdapter
        extends ListAdapter<MediaPlaybackInfo,OverviewItemAdapter.ViewHolder> {

    private List<MediaPlaybackInfo> mediaPlaybackInfoList;

    protected OverviewItemAdapter(@NonNull DiffUtil.ItemCallback<MediaPlaybackInfo> diffCallback) {
        super(diffCallback);
        mediaPlaybackInfoList = new ArrayList<>();
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
    @NonNull
    @Override
    public OverviewItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOverviewVideoSmallBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewItemAdapter.ViewHolder holder, int position) {
        holder.videoName.setText(mediaPlaybackInfoList.get(position).getMediaUri());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final LinearLayout videoClickArea;
        public final TextView videoName;

        public ViewHolder(ItemOverviewVideoSmallBinding binding) {
            super(binding.getRoot());
            videoClickArea = binding.videoClickAreaLinearlayout;
            videoName = binding.videoNameTextview;
        }
    }
}
