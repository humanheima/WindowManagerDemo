package com.hm.windowmanagerdemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.hm.windowmanagerdemo.R;
import com.hm.windowmanagerdemo.widget.FloatWindowBigView;
import com.hm.windowmanagerdemo.widget.FloatWindowSmallView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dumingwei on 2017/3/15.
 */
public class MyWindowManager {
    private static FloatWindowSmallView smallWindow;
    private static FloatWindowBigView bigWindow;

    private static WindowManager.LayoutParams smallLayoutParams;
    private static WindowManager.LayoutParams bigLayoutParams;

    private static WindowManager mWindowManage;

    //用于获取手机可用内存
    private static ActivityManager mActivityManager;

    /**
     * 创建一个小悬浮窗
     *
     * @param context 必须为应用程序的Context
     */
    public static void createSmallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatWindowSmallView(context);
            if (smallLayoutParams == null) {
                smallLayoutParams = new WindowManager.LayoutParams();
                smallLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallLayoutParams.format = PixelFormat.RGBA_8888;
                smallLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallLayoutParams.width = FloatWindowSmallView.viewWidth;
                smallLayoutParams.height = FloatWindowSmallView.viewHeight;
                smallLayoutParams.x = screenWidth;
                smallLayoutParams.y = screenHeight;
            }
            smallWindow.setParams(smallLayoutParams);
            windowManager.addView(smallWindow, smallLayoutParams);
        }
    }

    /**
     * 移除小悬浮窗
     *
     * @param context 必须为应用程序的Context
     */
    public static void removeSmallWindow(Context context) {

        if (smallWindow != null) {
            WindowManager manager = getWindowManager(context);
            manager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    /**
     * @param context 必须为应用程序的Context
     */
    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context);
            if (bigLayoutParams == null) {
                bigLayoutParams = new WindowManager.LayoutParams();
                bigLayoutParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
                bigLayoutParams.y = screenHeight / 2 - FloatWindowBigView.viewHeight / 2;
                bigLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                bigLayoutParams.format = PixelFormat.RGBA_8888;
                bigLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigLayoutParams.width = FloatWindowBigView.viewWidth;
                bigLayoutParams.height = FloatWindowBigView.viewHeight;
            }
            windowManager.addView(bigWindow, bigLayoutParams);
        }
    }

    /**
     * 移除大悬浮窗
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context) {

        if (bigWindow != null) {
            WindowManager manager = getWindowManager(context);
            manager.removeView(bigWindow);
            bigWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (smallWindow != null) {
            TextView textPercent = (TextView) smallWindow.findViewById(R.id.percent);
            textPercent.setText(getUsedPercentValue(context));
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }

    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "no memory";
    }

    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * @param context 必须为应用程序的Context.
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManage == null) {
            mWindowManage = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManage;
    }

}
