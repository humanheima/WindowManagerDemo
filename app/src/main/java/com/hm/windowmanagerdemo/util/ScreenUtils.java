package com.hm.windowmanagerdemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by dumingwei on 2017/3/15.
 */
public class ScreenUtils {

    private final static String TAG = "ScreenUtils";

    public static int getScreenWidth(Context context) {
        Point point = new Point();
        ((WindowManager) (context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay()
                .getSize(point);
        return point.x;
    }

    public static int getScreenHeight(Context context) {
        Point point = new Point();
        ((WindowManager) (context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay()
                .getSize(point);
        Log.e(TAG, "height=" + point.y);
        return point.y;
    }

    public static int getScreenHeight(Activity context) {
        WindowManager manager = context.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        Lg.e(TAG, "width=" + width + ",height=" + height);
        return height;
    }

}
