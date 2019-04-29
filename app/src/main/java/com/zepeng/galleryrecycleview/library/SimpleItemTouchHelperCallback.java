package com.zepeng.galleryrecycleview.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{

    private ItemTouchHelperAdapter mAdapter;
    private ItemTouchHelperScale mScale;
    private int currentDeletePosition ;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter ,ItemTouchHelperScale scale){
        mAdapter = adapter;
        mScale = scale;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.UP;
        return makeMovementFlags(0,swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        currentDeletePosition = viewHolder.getAdapterPosition();
        mAdapter.onItemDismiss(currentDeletePosition);
        mScale.onItemDismiss(currentDeletePosition);
    }
}
