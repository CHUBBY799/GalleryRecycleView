package com.zepeng.galleryrecycleview.library;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class ScaleDetectorConstraintLayout extends ConstraintLayout {
    private static final String TAG = "ScaleDetectorConstraint";
    private ScaleGestureDetector mScaleGestureDetector;

    public ScaleDetectorConstraintLayout(Context context) {
        super(context);
    }

    public ScaleDetectorConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScaleGestureDetector();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mScaleGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void initScaleGestureDetector() {
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Log.d(TAG, "sfocusX = " + detector.getFocusX());       // 缩放中心，x坐标
                Log.d(TAG, "sfocusY = " + detector.getFocusY());       // 缩放中心y坐标
                Log.d(TAG, "sscale = " + detector.getScaleFactor());
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d(TAG, "focusX = " + detector.getFocusX());       // 缩放中心，x坐标
                Log.d(TAG, "focusY = " + detector.getFocusY());       // 缩放中心y坐标
                Log.d(TAG, "scale = " + detector.getScaleFactor());   // 缩放因子
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        });
    }
}
