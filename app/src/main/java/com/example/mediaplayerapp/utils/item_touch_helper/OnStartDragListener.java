package com.example.mediaplayerapp.utils.item_touch_helper;

import androidx.recyclerview.widget.RecyclerView;

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}