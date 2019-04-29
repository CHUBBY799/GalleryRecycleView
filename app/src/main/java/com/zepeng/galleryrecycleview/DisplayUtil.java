package com.zepeng.galleryrecycleview;

import android.view.View;

public class DisplayUtil {
    private static final float ALPHA_OPAQUE = 1f;

    public static void ensureViewOpaque(View view) {
        if (view.getAlpha() < ALPHA_OPAQUE) {
            view.setAlpha(ALPHA_OPAQUE);
        }
    }
}
