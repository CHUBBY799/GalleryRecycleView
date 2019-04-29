package com.zepeng.galleryrecycleview;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreActivity extends AppCompatActivity {
    private static final String TAG = "PreActivity";
    private static final String INTENT_EXTRA_MEDIA_ITEM_POSITION = "intent_extra_media_item_position";

    RecyclerView mRecyclerView;
    PreAdapter mPreAdapter;
    List<ImageItem> mList = new ArrayList<>();
    Bundle mReenterState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);
        mList = DataManager.getInStance().getItems();
        mRecyclerView = findViewById(R.id.pre_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mPreAdapter = new PreAdapter(mList,this);
        mRecyclerView.setAdapter(mPreAdapter);

        initTransition();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        Log.d(TAG, "onActivityReenter: ");
        if (resultCode == RESULT_OK) {
            mReenterState = data.getExtras();
            int currentMediaItemPosition = mReenterState.getInt(MainActivity.EXTRA_DATA_MEDIA_ITEM_POSITION, 0);
            mReenterState.putInt(INTENT_EXTRA_MEDIA_ITEM_POSITION, currentMediaItemPosition);

            currentMediaItemPosition = checkItemPosInResult(currentMediaItemPosition,
                    mReenterState.getInt(MainActivity.EXTRA_DATA_MEDIA_ITEM_ID, -1));
            mReenterState.putInt(INTENT_EXTRA_MEDIA_ITEM_POSITION, currentMediaItemPosition);

            LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
            int firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            int lastCompletelyVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
            if (currentMediaItemPosition >= firstCompletelyVisibleItemPosition &&
                    currentMediaItemPosition <= lastCompletelyVisibleItemPosition) {
                /* the item is visible, do nothing */
            } else {
                postponeEnterTransition();
                mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
                        Log.d(TAG,"onPreDraw firstCompletelyVisibleItemPosition=" +manager.findFirstCompletelyVisibleItemPosition()
                                + " lastCompletelyVisibleItemPosition=" + manager.findLastCompletelyVisibleItemPosition());
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
                manager.scrollToPosition(currentMediaItemPosition);
//                mRecyclerView.smoothScrollToPosition(currentMediaItemPosition);
            }
            mReenterState.putInt(MainActivity.EXTRA_DATA_MEDIA_ITEM_POSITION, currentMediaItemPosition);
        }

    }

    public int checkItemPosInResult(int itemPos, int itemId) {
        return TransitionUtil.checkItemPosition(itemPos, itemId, mList);
    }

    private void initTransition() {
        ActivityCompat.setExitSharedElementCallback(this, new SharedElementCallback() {

            private void removeOldViews(List<String> names, Map<String, View> sharedElements) {
                List<String> namesTobeRemoved = new ArrayList<>();
                for (String name : names) {
                    if (TextUtils.isEmpty(name) || !name.startsWith("android")) {
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

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Log.d(TAG, "Pre onMapSharedElements: ");
                if (mReenterState != null) {
                    int currentMediaItemPosition = mReenterState.getInt(MainActivity.EXTRA_DATA_MEDIA_ITEM_POSITION, 0);
                    removeOldViews(names, sharedElements);
                    try {
                        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
                        View v = manager.findViewByPosition(currentMediaItemPosition);
                        View imageView = null;
                        if (v != null) {
                            imageView = v.findViewById(R.id.pre_image);
                        }
                        if (imageView != null && !TextUtils.isEmpty(imageView.getTransitionName())) {
                            sharedElements.put(imageView.getTransitionName(), imageView);
                            names.add(imageView.getTransitionName());
                            DisplayUtil.ensureViewOpaque(imageView);
                        }
                    } catch (Exception e) {
                        // catch exception for defect 5641102
                        Log.d(TAG, "exception in initTransition");
                        e.printStackTrace();
                    }
                    mReenterState = null;
                }
            }
        });
    }

}
