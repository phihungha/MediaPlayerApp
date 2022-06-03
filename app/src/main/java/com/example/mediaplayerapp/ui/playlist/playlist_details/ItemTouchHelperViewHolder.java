package com.example.mediaplayerapp.ui.playlist.playlist_details;

public interface ItemTouchHelperViewHolder {
    /**
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();

    /**
     * state should be cleared.
     */
    void onItemClear();
}