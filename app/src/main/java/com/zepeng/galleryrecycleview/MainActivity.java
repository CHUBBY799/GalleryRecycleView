package com.zepeng.galleryrecycleview;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.zepeng.galleryrecycleview.library.CardLinearSnapHelper;
import com.zepeng.galleryrecycleview.library.CardScaleHelper;
import com.zepeng.galleryrecycleview.library.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private List<ImageItem> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private CardAdapter mCardAdapter;
    private PreviewElementCallback mElementCallback = new PreviewElementCallback();
    private ActionBar mSupportActionBar;
    private int initItemId;

    public static final String SHARED_ELEMENT_NAME_FORMAT = "image_%d";
    public final static String EXTRA_DATA_MEDIA_ITEM_POSITION = "media_item_position";
    public final static String EXTRA_DATA_MEDIA_ITEM_ID = "media_item_id";


    public static String createTransitionName(int mediaItemId){
        return String.format(Locale.ENGLISH, SHARED_ELEMENT_NAME_FORMAT,mediaItemId);
    }
    public static void gotoInternalPreviewActivity(Activity activity,int imageId, View... sharedViews) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_DATA_MEDIA_ITEM_ID, imageId);

        if (sharedViews != null && sharedViews.length > 0) {
            List<Pair<View, String>> viewPairs = new ArrayList<>();
            for (View view : sharedViews) {
                if (!TextUtils.isEmpty(view.getTransitionName())) {
                    Log.d(TAG, "gotoInternalPreviewActivity: "+view.getTransitionName());
                    viewPairs.add(Pair.create(view, view.getTransitionName()));
                }
            }

            Pair<View, String>[] pairs = (Pair<View, String>[]) new Pair[viewPairs.size()];
            viewPairs.toArray(pairs);
            // TODO to trace a bug of launching InternalPreviewActivity   20190420
            Log.d(TAG, "go to preview 1");
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity, pairs).toBundle());
        } else {
            Log.d(TAG, "go to preview 2");
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        supportPostponeEnterTransition();
        init();
        initActionBar();
        initTransition();
    }

    protected void initActionBar() {
        mSupportActionBar = getSupportActionBar();
        if(mSupportActionBar == null){
            return;
        }
        mSupportActionBar.setBackgroundDrawable(null);
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSupportActionBar.setElevation(0);
        }
    }
    private void initTransition() {
//        supportPostponeEnterTransition();
        setEnterSharedElementCallback(mElementCallback);
    }

    private void setupReturnTransition() {
        Log.d(TAG, "setupReturnTransition: ");
        int position = mCardScaleHelper.getCurrentItemPos();
        LinearLayoutManager manager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        View viewGroup = manager.findViewByPosition(position);
        if (viewGroup == null) {
            return;
        }
        ImageView imageView = viewGroup.findViewById(R.id.imageView);
        mElementCallback.setSharedElements(imageView);
    }

    @Override
    public void onBackPressed() {
        setupReturnTransition();
        super.onBackPressed();
    }

    private boolean mIsEnteringTransition = true;

    private void init() {
        initItemId = getIntent().getIntExtra(MainActivity.EXTRA_DATA_MEDIA_ITEM_ID,0);
        mList = DataManager.getInStance().getItems();
        mRecyclerView =  findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCardAdapter = new CardAdapter(mList, this, new CardAdapter.Callback() {
            @Override
            public void onBindViewHolder(int position, CardAdapter.ViewHolder holder) {
                if(mIsEnteringTransition && position == findPositionById(initItemId)){
                    Log.d(TAG, "onBindViewHolder: "+position);
                    mIsEnteringTransition = false;
                    mElementCallback.setSharedElements(holder.mImageView);
                }
            }
        });
        mRecyclerView.setAdapter(mCardAdapter);
//         mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(findPositionById(initItemId));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCardAdapter,mCardScaleHelper);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        scrollToCurrentTransitionPosition(findPositionById(initItemId));
    }

    private int findPositionById(int id){
        for(int i = 0 ; i<mList.size() ; i++){
            if(mList.get(i).id == id){
                return i ;
            }
        }
        return 0;
    }

    @Override
    public void finishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(EXTRA_DATA_MEDIA_ITEM_POSITION, mCardScaleHelper.getCurrentItemPos());
        data.putExtra(EXTRA_DATA_MEDIA_ITEM_ID, mList.get(mCardScaleHelper.getCurrentItemPos()).id);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }

    public int scrollToCurrentTransitionPosition(int viewPosition) {
        int offset = 0;
        /**
         * if mPresenter is null object, return position directly.
         */

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        if (viewPosition >= firstCompletelyVisibleItemPosition &&
                viewPosition <= lastCompletelyVisibleItemPosition) {
            /* the item is visible, do nothing */
            mCardScaleHelper.attachToRecyclerView(mRecyclerView);
            return viewPosition;
        } else {
            supportPostponeEnterTransition();
            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    int firstV = layoutManager.findFirstVisibleItemPosition();
                    int lastV = layoutManager.findLastVisibleItemPosition();
                    Log.d(TAG, "onPreDraw: "+firstCompletelyVisibleItemPosition+" "+lastCompletelyVisibleItemPosition);
                    Log.d(TAG, "onPreDraw: "+firstV+" "+lastV+" "+layoutManager.getChildCount());

                    mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                    supportStartPostponedEnterTransition();
                    mCardScaleHelper.attachToRecyclerView(mRecyclerView);
                    return true;
                }
            });
            Log.d(TAG, "scrollToCurrentTransitionPosition: "+viewPosition);
            ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(viewPosition,ScreenUtil.dip2px(this, 30));

            return viewPosition;
        }
    }
}
