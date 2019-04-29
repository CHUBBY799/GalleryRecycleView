package com.zepeng.galleryrecycleview;

import android.support.v4.app.SharedElementCallback;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PreviewElementCallback extends SharedElementCallback {
    private static final String TAG = "PreviewElementCallback";

    private List<View> mSharedViews = new ArrayList<>();

    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        Log.d(TAG, "main onMapSharedElements: ");
        removeOldViews(names, sharedElements);
        if (mSharedViews.size() > 0) {
            Log.d(TAG, "onMapSharedElements: ");
            for (View view : mSharedViews) {
                if (!TextUtils.isEmpty(view.getTransitionName())) {
                    Log.d(TAG, "onMapSharedElements: "+view.getTransitionName());
                    names.add(view.getTransitionName());
                    sharedElements.put(view.getTransitionName(), view);
                }
            }
        }
    }

    public void setSharedElements(View... views) {
        mSharedViews.clear();
        mSharedViews.addAll(Arrays.asList(views));
    }

    private void removeOldViews(List<String> names, Map<String, View> sharedElements) {
        List<String> namesTobeRemoved = new ArrayList<>();
        for (String name : names) {
            if (!name.startsWith("android")) {
                namesTobeRemoved.add(name);
            }
        }
        if (namesTobeRemoved.size() > 0) {
            names.removeAll(namesTobeRemoved);
            for (String name : namesTobeRemoved) {
                sharedElements.remove(name);
            }
        }
    }
}
