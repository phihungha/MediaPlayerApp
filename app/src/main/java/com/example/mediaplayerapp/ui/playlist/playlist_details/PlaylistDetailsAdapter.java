package com.example.mediaplayerapp.ui.playlist.playlist_details;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.Video;

public class PlaylistDetailsAdapter extends ListAdapter<Video,PlaylistDetailsViewHolder> {
    private Context mContext;

    protected PlaylistDetailsAdapter(@NonNull DiffUtil.ItemCallback<Video> diffCallback) {
        super(diffCallback);
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PlaylistDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlaylistDetailsViewHolder.create(parent,mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDetailsViewHolder holder, int position) {
        Video current=getPlaylistVideoItemAt(position);

        holder.setBinding(current);

    }

    static class PlaylistMediaDiff extends DiffUtil.ItemCallback<Video> {
        @Override
        public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
            return oldItem.getUri().equals(newItem.getUri());
        }
    }

    public Video getPlaylistVideoItemAt(int position){
        return getItem(position);
    }
}



/*
{
private Context mContext;
private ArrayList<PlaylistMediaModel> mArrayListVideos;
private Activity mActivity;

public void setArrayListVideos(ArrayList<PlaylistMediaModel> mArrayListVideos) {
        this.mArrayListVideos = mArrayListVideos;
        notifyDataSetChanged();
        }

@NonNull
@Override
public PlaylistDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);

        return new PlaylistDetailsViewHolder(binding);
        */
/*return new PlaylistDetailsViewHolder(ItemPlaylistDetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));*//*

        }

@Override
public void onBindViewHolder(@NonNull PlaylistDetailsViewHolder holder, int position) {
        PlaylistMediaModel video = mArrayListVideos.get(position);
        if (video==null){
        return;
        }
       */
/* Glide.with(mContext)
                .load("file://"+ video.getThumb())
                .skipMemoryCache(false)
                .into(holder.binding.imgThumbnailPlaylistDetails);*//*

        holder.binding.tvPlaylistNamePlaylistDetails.setText(video.getName());

        holder.binding.layoutItemPlaylistDetails.setBackgroundColor(Color.parseColor(PlaylistConstants.COLOR_WHITE));
        holder.binding.layoutItemPlaylistDetails.setAlpha(0);

        holder.binding.layoutItemPlaylistDetails.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
                */
/*Intent i =new Intent(mContext, VideoPlayerActivity.class);
                i.putExtra(PlaylistConstants.EXTRA_VIDEO,video.getPath());
                mActivity.startActivity(i);*//*


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
*/
