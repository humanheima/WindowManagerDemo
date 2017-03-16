package com.hm.windowmanagerdemo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.windowmanagerdemo.R;
import com.hm.windowmanagerdemo.util.MyWindowManager;

import java.lang.reflect.Field;

/**
 * Created by dumingwei on 2017/3/15.
 */
public class FloatWindowSmallView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    private static int statusBarHeight;

    //记录当前手指位置在屏幕上的横坐标
    private float xInScreen;
    //记录当前手指位置在屏幕上的纵坐标
    private float yInScreen;
    //记录当前手指按下时在屏幕上的横坐标
    private float xDownInScreen;
    //记录当前手指按下时在屏幕上的纵坐标
    private float yDownInScreen;
    //记录手指按下时在小悬浮窗的View上的横坐标的值
    private float xInView;
    //记录手指按下时在小悬浮窗的View上的纵坐标的值
    private float yInView;

    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;

    public FloatWindowSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        TextView percentView = (TextView) findViewById(R.id.percent);
        percentView.setText(MyWindowManager.getUsedPercentValue(context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    openBigWindow();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    private void openBigWindow() {
        MyWindowManager.createBigWindow(getContext());
        MyWindowManager.removeSmallWindow(getContext());
    }

    private float getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (int) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
}
