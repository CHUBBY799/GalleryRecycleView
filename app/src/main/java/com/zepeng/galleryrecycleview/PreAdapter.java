package com.zepeng.galleryrecycleview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PreAdapter extends RecyclerView.Adapter<PreAdapter.ViewHolder>{
    private static final String TAG = "PreAdapter";
    List<ImageItem> datas;
    Activity mActivity;

    public PreAdapter(List datas,Activity activity) {
        this.datas = datas;
        mActivity = activity;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_pre_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final ImageItem imageItem = datas.get(i);
        viewHolder.mImageView.setImageResource(imageItem.resourceId);
        Log.d(TAG, "onBindViewHolder: "+MainActivity.createTransitionName(imageItem.id));
        viewHolder.mImageView.setTransitionName(MainActivity.createTransitionName(imageItem.id));
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.gotoInternalPreviewActivity(mActivity,imageItem.id,viewHolder.mImageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pre_image);
        }
    }
}
