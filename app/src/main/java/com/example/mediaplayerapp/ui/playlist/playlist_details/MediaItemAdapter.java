package com.example.mediaplayerapp.ui.playlist.playlist_details;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mediaplayerapp.data.playlist.Playlist;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItem;
import com.example.mediaplayerapp.data.playlist.playlist_details.PlaylistItemViewModel;
import com.example.mediaplayerapp.ui.music_library.DisplayMode;
import com.example.mediaplayerapp.ui.playlist.IOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MediaItemAdapter extends ListAdapter<PlaylistItem, MediaItemViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private IOnItemClickListener itemClickListener;
    private IOnItemClickListener bsPlayListener;
    private IOnItemClickListener bsDeleteListener;
    private IOnItemClickListener bsPropertiesListener;
    private IOnItemClickListener bsAddQueueListener;
    private IOnItemClickListener bsAddFavouriteListener;

    private Application mApplication;
    private Playlist mPlaylist;
    private OnStartDragListener mDragStartListener;
    private OnPlaylistItemListChangedListener mListChangedListener;

    private DisplayMode displayMode = DisplayMode.LIST;

    private PlaylistItemViewModel viewModel;
    private static ArrayList<Integer> listPos;
    private static ArrayList<PlaylistItem> listBeforeDrag;

    public MediaItemAdapter(@NonNull DiffUtil.ItemCallback<PlaylistItem> diffCallback) {
        super(diffCallback);
        listPos = new ArrayList<>();
        listBeforeDrag = new ArrayList<>();
        viewModel=new PlaylistItemViewModel(mApplication);
    }

    @NonNull
    @Override
    public MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MediaItemViewHolder.create(parent, mContext, itemClickListener,
                bsPlayListener, bsDeleteListener, bsPropertiesListener, bsAddQueueListener, bsAddFavouriteListener,
                mPlaylist, this,displayMode);
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public int getItemViewType(int position) {
        return displayMode.getValue();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MediaItemViewHolder holder, int position) {
        PlaylistItem current = getPlaylistMediaItemAt(position);
        if (current == null) {
            return;
        }
        Uri uri = Uri.parse(current.getMediaUri());
        if (MediaUtils.isUriExists(mContext, uri)) {
            if (displayMode==DisplayMode.LIST){
                holder.setBindingList(current);
                holder.binding.imgThumbnailPlaylistDetails.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            mDragStartListener.onStartDrag(holder);
                        }
                        return true;
                    }
                });
            }
            else {
                holder.setBindingGrid(current);
                holder.gridBinding.imgThumbPlaylistDetailGrid.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            mDragStartListener.onStartDrag(holder);
                        }
                        return true;
                    }
                });
            }

        } else {
            PlaylistItemViewModel playlistItemViewModel = new PlaylistItemViewModel(mApplication);
            playlistItemViewModel.deleteItemWithUri(uri.toString());
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        listPos.add(fromPosition);
        listPos.add(toPosition);

   /*     if (listBeforeDrag.size() < 1) {
            listBeforeDrag = new ArrayList<>(getCurrentList());
        }
*/
        notifyItemMoved(fromPosition,toPosition);
    }

    public void swapItem() {
        int fromPosition = getFirstPos();
        int toPosition = getSecondPos();

        PlaylistItemViewModel viewModel=new PlaylistItemViewModel(mApplication);
        List<PlaylistItem> current=viewModel.getCurrentList();

        if (fromPosition < toPosition) {
            long key = current.get(toPosition).getOrderSort();
            for (int i = toPosition; i > fromPosition; i--) {
                PlaylistItem item = current.get(i);
                PlaylistItem pre=current.get(i-1);
                item.setOrderSort(pre.getOrderSort());
                viewModel.update(item);
            }

            PlaylistItem lastItem=current.get(fromPosition);
            lastItem.setOrderSort(key);
            viewModel.update(lastItem);
        }
/*        for (PlaylistItem item :
                listBeforeDrag) {
            Log.d("TAG_LIST_BEFORE",item.getName() + " " + String.valueOf(item.getOrderSort()));
        }

        PlaylistItem first = listBeforeDrag.get(fromPosition);
        PlaylistItem second = listBeforeDrag.get(toPosition);
        Log.d("TAG_LIST_FIRST",first.getName());
        Log.d("TAG_LIST_SECOND",second.getName());*/

        //MediaUtils.insertionSort(mApplication,listBeforeDrag,fromPosition,toPosition);
        //MediaUtils.updateListToDatabase(mApplication,newList);
        //MediaUtils.swapOrderOf(mApplication, current);
      /*  PlaylistItem first = current.get(fromPosition);
        PlaylistItem second = current.get(toPosition);

        for (PlaylistItem item :
                current) {
            Log.d("TAG_LIST_CURRENT",item.getName());
        }

        Log.d("TAG_LIST_FIRST",first.getName());
        Log.d("TAG_LIST_SECOND",second.getName());*/
    }

    public int getListPosSize() {
        return listPos.size();
    }

    public int getFirstPos() {
        return listPos.get(0);
    }

    public void clearList() {
        listPos.clear();
        listBeforeDrag.clear();
    }

    public int getSecondPos() {
        return listPos.get(listPos.size() - 1);
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d("TAG", "DISMISS");
    }

    public static class PlaylistMediaDiff extends DiffUtil.ItemCallback<PlaylistItem> {
        @Override
        public boolean areItemsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem.getMediaUri().equals(newItem.getMediaUri())
                    && oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull PlaylistItem oldItem, @NonNull PlaylistItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    public PlaylistItem getPlaylistMediaItemAt(int position) {
        return getItem(position);
    }
    public void setDragStartListener(OnStartDragListener mDragStartListener) {
        this.mDragStartListener = mDragStartListener;
    }

    public void setListChangedListener(OnPlaylistItemListChangedListener mListChangedListener) {
        this.mListChangedListener = mListChangedListener;
    }

    public void setPlaylist(Playlist mPlaylist) {
        this.mPlaylist = mPlaylist;
    }

    public void setBsAddQueueListener(IOnItemClickListener bsAddQueueListener) {
        this.bsAddQueueListener = bsAddQueueListener;
    }

    public void setBsAddFavouriteListener(IOnItemClickListener bsAddFavouriteListener) {
        this.bsAddFavouriteListener = bsAddFavouriteListener;
    }

    public void setApplication(Application mApplication) {
        this.mApplication = mApplication;
    }

    public void setBsPropertiesListener(IOnItemClickListener bsPropertiesListener) {
        this.bsPropertiesListener = bsPropertiesListener;
    }

    public void setItemClickListener(IOnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setBsPlayListener(IOnItemClickListener bsPlayListener) {
        this.bsPlayListener = bsPlayListener;
    }

    public void setBsDeleteListener(IOnItemClickListener bsDeleteListener) {
        this.bsDeleteListener = bsDeleteListener;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

}
