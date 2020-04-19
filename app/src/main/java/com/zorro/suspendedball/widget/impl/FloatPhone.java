package com.zorro.suspendedball.widget.impl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.zorro.suspendedball.permission.FloatActivity;
import com.zorro.suspendedball.permission.PermissionListener;
import com.zorro.suspendedball.permission.room.Miui;
import com.zorro.suspendedball.widget.FloatLifecycleReceiver;
import com.zorro.suspendedball.widget.LifecycleListener;
import com.zorro.suspendedball.widget.interfaces.BaseFloatView;
import com.zorro.suspendedball.widget.interfaces.OnFloatCallbacks;


/**
 * @Copyright © 2017 Analysys Inc. All rights reserved.
 * @Description:
 * @Version: 1.0.9
 * @Create: 2017-11-14 17:15:35
 * @Author: yhao
 */
public class FloatPhone extends BaseFloatView {
    private FloatLifecycleReceiver floatLifecycle;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmLayoutParams;
    private boolean isRemove = true;
    private boolean isShow = false;
    private FloatView floatView;

    public FloatPhone(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmLayoutParams = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        // 设置背景透明
        wmLayoutParams.format = PixelFormat.RGBA_8888;

        wmLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 确定悬浮窗的对齐方式
        wmLayoutParams.gravity = Gravity.START | Gravity.TOP;
        // 设置悬浮层初始位置
        wmLayoutParams.x = screenWidth;
        wmLayoutParams.y = screenHeight / 8;

        wmLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //addFloatView();
    }


    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        wmLayoutParams.gravity = gravity;
        wmLayoutParams.x = xOffset;
        wmLayoutParams.y = yOffset;
    }

    @Override
    public void init() {
       //
        createFloatingWindow();
    }

    private void createFloatingWindow() {
        removeFloatView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            req();
        } else if (Miui.rom()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                req();
            } else {
                wmLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                Miui.req(mContext.getApplicationContext(), new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        addFloatView();
                    }

                    @Override
                    public void onFail() {
                    }
                });
            }
        } else {
            try {
                wmLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                addFloatView();
            } catch (Exception e) {
                removeFloatView();
                req();
            }
        }
    }

    private void req() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        FloatActivity.request(mContext.getApplicationContext(), new PermissionListener() {
            @Override
            public void onSuccess() {
                addFloatView();
            }

            @Override
            public void onFail() {

            }
        });

    }

    /**
     * 添加FloatView到WindowManager
     */
    private void addFloatView() {
        if (floatView == null) {
            floatView = new FloatView(mContext);
        }
        floatView.setWindowMangerLayoutParams(wmLayoutParams);
        floatView.setOnFloatCallbacks(new OnFloatCallbacks() {
            @Override
            public void updateLayoutParams(WindowManager.LayoutParams params) {
                if (mWindowManager != null && !isRemove) {
                    mWindowManager.updateViewLayout(floatView, params);
                }
            }

            @Override
            public void onClick(View v) {

            }
        });
        mWindowManager.addView(floatView, wmLayoutParams);
        isRemove = false;
        isShow = true;
        floatLifecycle = new FloatLifecycleReceiver(mContext, new LifecycleListener() {
            @Override
            public void onShow() {
                if (!isShow) {
                    show();
                }
            }

            @Override
            public void onHide() {

            }

            @Override
            public void onBackToDesktop() {
                if (isShow) {
                    hide();
                }
            }
        });
    }

    /**
     * 从WindowManager移除FloatView
     */
    private void removeFloatView() {
        isRemove = true;
        isShow = false;
        if (floatView != null) {
            mWindowManager.removeView(floatView);
            floatView = null;
        }
    }

    @Override
    public void show() {
        if (floatView != null) {
            floatView.show();
            isShow = true;
        }
    }

    @Override
    public void hide() {
        if (floatView != null) {
            floatView.hide();
            isShow = false;
        }
    }

    @Override
    public void dismiss() {
        removeFloatView();
    }

    @Override
    public void destroy() {
        isRemove = true;
        isShow = false;
        if (floatView != null) {
            floatView.destroy();
        }
        if (floatLifecycle != null) {
            floatLifecycle.unRegisterReceiver(mContext);
        }
    }


}
