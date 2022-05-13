package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistMedia;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static Context mContext;
    private ItemPlaylistDetailsBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private static IOnItemClickListener itemClickListener;
    private static IOnItemClickListener bsPlayListener;
    private static IOnItemClickListener bsDeleteListener;
    private static IOnItemClickListener bsPropertiesListener;
    private static IOnItemClickListener bsAddQueueListener;

    public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
        this.binding.layoutItemPlaylistDetails.setOnClickListener(this);
        this.binding.imgBtnPlaylistDetailsMore.setOnClickListener(this);
    }

    public void setBinding(PlaylistMedia media) {
        binding.tvPlaylistNamePlaylistDetails.setText(media.getName());
        MediaInfo mediaInfo=MediaUtils.getInfoWithUri(mContext, Uri.parse(media.getMediaUri()));
        String duration=MediaUtils.convertDuration(mediaInfo.getDuration());
        binding.tvDurationMedia.setText(duration);

        Glide.with(mContext)
                .load(media.getMediaUri())
                .skipMemoryCache(false)
                .error(R.drawable.ic_round_error_24)
                .centerCrop()
                .into(binding.imgThumbnailPlaylistDetails);
    }

    static PlaylistDetailsViewHolder create(ViewGroup parent,
                                            Context context,
                                            IOnItemClickListener _itemClickListener,
                                            IOnItemClickListener _bsPlayListener,
                                            IOnItemClickListener _bsDeleteListener,
                                            IOnItemClickListener _bsPropertiesListener,
                                            IOnItemClickListener _bsAddQueueListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);
        itemClickListener=_itemClickListener;
        bsPlayListener=_bsPlayListener;
        bsDeleteListener=_bsDeleteListener;
        bsPropertiesListener=_bsPropertiesListener;
        bsAddQueueListener=_bsAddQueueListener;
        mContext=context;
        return new PlaylistDetailsViewHolder(binding);
    }

    private void onClickItem(){
        if (itemClickListener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            itemClickListener.onClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutItem_mediaQueue:
                onClickItem();
                break;

            case R.id.imgBtn_playlist_details_more:
                openBottomSheetDialog();
                break;

            case R.id.bs_startPlaylistDetailsItem:
                playNext();
                break;

            case R.id.bs_deletePlaylistDetailsItem:
                deleteItem();
                break;

            case R.id.bs_propertiesPlaylistDetailsItem:
                openProperties();
                break;

            case R.id.bs_addToQueuePlaylistDetailsItem:
                addToQueue();
                break;
        }
    }

    private void addToQueue() {
        bsAddQueueListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void openProperties() {
        bsPropertiesListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void deleteItem() {
        bsDeleteListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void playNext() {
        bsPlayListener.onClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    /**
     * Open BottomSheet Dialog when click to edit the item media
     */
    private void openBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(itemView.getContext(), R.style.BottomSheetTheme);
        View bsView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.playlist_details_bs_layout,
                itemView.findViewById(R.id.bs_playlist_detail));

        TextView tv_name = bsView.findViewById(R.id.tv_playlist_details_name);
        tv_name.setText(binding.tvPlaylistNamePlaylistDetails.getText().toString());

        bsView.findViewById(R.id.bs_startPlaylistDetailsItem).setOnClickListener(this);
        bsView.findViewById(R.id.bs_deletePlaylistDetailsItem).setOnClickListener(this);
        bsView.findViewById(R.id.bs_propertiesPlaylistDetailsItem).setOnClickListener(this);
        bsView.findViewById(R.id.bs_addToQueuePlaylistDetailsItem).setOnClickListener(this);

        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }
}
