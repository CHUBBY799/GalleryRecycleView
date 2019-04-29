package com.zepeng.galleryrecycleview;

import java.util.List;

public class TransitionUtil {

    /**
     * In shared element transition between activities, the data list is changing,
     * so we should check the item position.
     * If the item in itemPos doesn't match itemId, then find out the real position.
     * @param itemPos item position in result intent.
     * @param itemId item id in result intent.
     * @return real position.
     */
    public static int checkItemPosition(int itemPos, int itemId, List<ImageItem> itemList) {
        int realPos = itemPos;
        boolean hasFound = false;
        if (itemList != null && itemList.size() > 0 && itemPos < itemList.size() && itemPos >= 0 && itemId >= 0) {
            if (itemList.get(itemPos).id != itemId) {
                // seek forward
                for (int i = itemPos + 1; i < itemList.size(); i++) {
                    if (itemList.get(i).id == itemId) {
                        realPos = i;
                        hasFound = true;
                        break;
                    }
                }
                if (!hasFound) {
                    // seek backward
                    for (int i = itemPos - 1; i >= 0; i--) {
                        if (itemList.get(i).id == itemId) {
                            realPos = i;
                            //hasFound = true;
                            break;
                        }
                    }
                }
            }
        }
        return realPos;
    }
}
