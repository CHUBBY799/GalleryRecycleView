package com.zepeng.galleryrecycleview;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<ImageItem> mItems = new ArrayList<>();
    private DataManager(){
        for (int i = 0; i < 10; i++) {
            mItems.add(new ImageItem((String.valueOf(i)+"pic4").hashCode(),R.drawable.pic4));
            mItems.add(new ImageItem((String.valueOf(i)+"pic5").hashCode(),R.drawable.pic5));
            mItems.add(new ImageItem((String.valueOf(i)+"pic6").hashCode(),R.drawable.pic6));
        }
    }

    public static DataManager getInStance(){
        return Instance.mInstance;
    }

    private static class Instance{
       private static final DataManager mInstance = new DataManager();
    }

    public List<ImageItem> getItems() {
        List<ImageItem> imageItems = new ArrayList<>();
        imageItems.addAll(mItems);
        return imageItems;
    }
}
