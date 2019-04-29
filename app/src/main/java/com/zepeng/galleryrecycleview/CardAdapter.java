package com.zepeng.galleryrecycleview;


import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zepeng.galleryrecycleview.library.CardAdapterHelper;
import com.zepeng.galleryrecycleview.library.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jameson on 8/30/16.
 */
class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "CardAdapter";
    private List<ImageItem> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    private Activity mActivity;
    private Callback mCallback;
    public CardAdapter(List<ImageItem> mList, Activity activity,Callback callback) {
        this.mList = mList;
        mActivity = activity;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageItem imageItem = mList.get(position);
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        holder.mImageView.setImageResource(imageItem.resourceId);
        Log.d(TAG, "onBindViewHolder: "+MainActivity.createTransitionName(imageItem.id));
        holder.mImageView.setTransitionName(
                MainActivity.createTransitionName(imageItem.id));
        if(mCallback != null){
            mCallback.onBindViewHolder(holder.getLayoutPosition(),holder);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public static interface Callback{
        void onBindViewHolder(int position,ViewHolder holder);
    }

}
