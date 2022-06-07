package com.example.mediaplayerapp.utils.item_touch;

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