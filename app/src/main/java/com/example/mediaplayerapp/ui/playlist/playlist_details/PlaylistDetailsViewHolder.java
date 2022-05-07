package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.databinding.ItemPlaylistDetailsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class PlaylistDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static Context mContext;
    private ItemPlaylistDetailsBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private static IOnPlaylistDetailsItemClickListener itemClickListener;
    private static IOnPlaylistDetailsItemClickListener bsPlayListener;
    private static IOnPlaylistDetailsItemClickListener bsDeleteListener;
    private static IOnPlaylistDetailsItemClickListener bsPropertiesListener;

    public PlaylistDetailsViewHolder(@NonNull ItemPlaylistDetailsBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
        this.binding.layoutItemPlaylistDetails.setOnClickListener(this);
        this.binding.imgBtnPlaylistDetailsMore.setOnClickListener(this);
    }

    public void setBinding(PlaylistMedia media) {
        binding.tvPlaylistNamePlaylistDetails.setText(media.getName());

        Glide.with(mContext)
                .load(media.getMediaUri())
                .skipMemoryCache(false)
                .error(R.drawable.ic_round_error_24)
                .centerCrop()
                .into(binding.imgThumbnailPlaylistDetails);
    }

    static PlaylistDetailsViewHolder create(ViewGroup parent,
                                            Context context,
                                            IOnPlaylistDetailsItemClickListener _itemClickListener,
                                            IOnPlaylistDetailsItemClickListener _bsPlayListener,
                                            IOnPlaylistDetailsItemClickListener _bsDeleteListener,
                                            IOnPlaylistDetailsItemClickListener _bsPropertiesListener) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistDetailsBinding binding = ItemPlaylistDetailsBinding.inflate(inflater, parent, false);
        itemClickListener=_itemClickListener;
        bsPlayListener=_bsPlayListener;
        bsDeleteListener=_bsDeleteListener;
        bsPropertiesListener=_bsPropertiesListener;
        mContext=context;
        return new PlaylistDetailsViewHolder(binding);
    }

    private void onClickItem(){
        if (itemClickListener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
            itemClickListener.OnClick(itemView.getRootView(), getBindingAdapterPosition());
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutItem_playlistDetails:
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
        }
    }

    private void openProperties() {
        bsPropertiesListener.OnClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void deleteItem() {
        bsDeleteListener.OnClick(itemView,getBindingAdapterPosition());
        bottomSheetDialog.dismiss();
    }

    private void playNext() {
        bsPlayListener.OnClick(itemView,getBindingAdapterPosition());
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

        bottomSheetDialog.setContentView(bsView);
        bottomSheetDialog.show();
    }
}
