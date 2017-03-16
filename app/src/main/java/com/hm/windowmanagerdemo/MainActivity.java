package com.hm.windowmanagerdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.hm.windowmanagerdemo.service.FloatViewService;
import com.hm.windowmanagerdemo.util.ScreenUtils;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.btn_float_window)
    Button btnFloatWindow;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private Button btnView;
    private boolean isAdded;
    private ActivityManager mActivityManager;
    private HashSet homeList;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ScreenUtils.getScreenHeight(this);
        addWindow();
    }

    /**
     * 通过WindowManager添加Window
     */
    private void addWindow() {
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.guide);
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0, PixelFormat.RGBA_8888);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.width = ScreenUtils.getScreenWidth(MainActivity.this);
        layoutParams.height = ScreenUtils.getScreenHeight(MainActivity.this);
        windowManager.addView(imageView, layoutParams);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(imageView);
                imageView = null;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (imageView != null) {
            Log.e(TAG, "onBackPressed imageView.getVisibility() == View.VISIBLE");
            windowManager.removeView(imageView);
            imageView = null;
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btn_float_window)
    public void onClick() {
        Intent intent = new Intent(this, FloatViewService.class);
        startService(intent);
        finish();
    }
}
