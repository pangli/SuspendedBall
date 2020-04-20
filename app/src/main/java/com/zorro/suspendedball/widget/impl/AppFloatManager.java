package com.zorro.suspendedball.widget.impl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.zorro.suspendedball.config.FloatConfig;
import com.zorro.suspendedball.widget.interfaces.OnFloatCallbacks;


/**
 * @Copyright © 2017 Analysys Inc. All rights reserved.
 * @Description:
 * @Version: 1.0.9
 * @Create: 2017-11-14 17:15:35
 * @Author: yhao
 */
public class AppFloatManager {
    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private FloatView floatView;
    private FloatConfig config;


    public FloatConfig getConfig() {
        return config;
    }

    public AppFloatManager(Context context, FloatConfig config) {
        this.context = context;
        this.config = config;
    }

    public void createFloat() {
        initParams();
        addView();
        config.setShow(true);
    }


    private void initParams() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        // 安卓6.0 以后，全局的Window类别，必须使用TYPE_APPLICATION_OVERLAY
        params.type = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        // 设置背景透明
        params.format = PixelFormat.RGBA_8888;
        // 设置浮窗以外的触摸事件可以传递给后面的窗口、不自动获取焦点
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 确定悬浮窗的对齐方式
        params.gravity = Gravity.START | Gravity.TOP;
        // 设置悬浮层初始位置
        params.x = screenWidth;
        params.y = screenHeight / 8;
        //宽高设置
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 添加FloatView到WindowManager
     */
    private void addView() {
        floatView = new FloatView(context);
        floatView.setWindowMangerLayoutParams(params);
        floatView.setOnFloatCallbacks(new OnFloatCallbacks() {
            @Override
            public void updateLayoutParams(WindowManager.LayoutParams params) {
                if (windowManager != null && floatView != null) {
                    windowManager.updateViewLayout(floatView, params);
                }
            }

            @Override
            public void onClick(View v) {
                if (config.getCallbacks() != null) {
                    config.getCallbacks().onClick(v);
                }
            }
        });
        windowManager.addView(floatView, params);
        config.setLayoutView(floatView);
    }

    /**
     * 设置浮窗的可见性,用户主动调用隐藏时，needShow需要为false
     */
    public void setVisible(int visible, boolean needShow) {
        if (floatView != null) {
            // 如果用户主动隐藏浮窗，则该值为false
            config.setNeedShow(needShow);
            if (visible == View.VISIBLE) {
                config.setShow(true);
                floatView.show();
            } else {
                config.setShow(false);
                floatView.hide();
            }
        }
    }

    public void dismiss() {
        removeView();
    }

    /**
     * 从WindowManager移除FloatView
     */
    private void removeView() {
        if (floatView != null) {
            floatView.destroy();
            if (windowManager != null) {
                windowManager.removeView(floatView);
            }
            floatView = null;
        }
    }
}
