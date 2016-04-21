package com.zpauly.pickers.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by root on 16-4-20.
 */
public class DevicesUtils {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
