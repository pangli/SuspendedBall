package com.zorro.suspendedball.widget;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by yhao on 17-12-1.
 * 用于控制悬浮窗显示周期
 * 使用了三种方法针对返回桌面时隐藏悬浮按钮
 * 1.startCount计数，针对back到桌面可以及时隐藏
 * 2.监听home键，从而及时隐藏
 * 3.resumeCount计时，针对一些只执行onPause不执行onStop的奇葩情况
 */

public class FloatLifecycleReceiver extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final long delay = 300;
    private Handler mHandler;
    private int startCount;
    private int resumeCount;
    private boolean appBackground;
    private LifecycleListener mLifecycleListener;
    private static ResumedListener sResumedListener;
    private static int num = 0;


    /**
     * @param context
     * @param lifecycleListener
     */
    public FloatLifecycleReceiver(Context context, LifecycleListener lifecycleListener) {
        num++;
        mLifecycleListener = lifecycleListener;
        mHandler = new Handler();
        if (context.getApplicationContext() instanceof Application) {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(this);
        } else {
            Log.e("FloatLifecycleReceiver", "必须是ApplicationContext");
            return;
        }
        context.registerReceiver(this,
                new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public static void setResumedListener(ResumedListener resumedListener) {
        sResumedListener = resumedListener;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (sResumedListener != null) {
            num--;
            if (num == 0) {
                sResumedListener.onResumed();
                sResumedListener = null;
            }
        }
        resumeCount++;
        mLifecycleListener.onShow();
        if (appBackground) {
            appBackground = false;
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        resumeCount--;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (resumeCount == 0) {
                    appBackground = true;
                    if (mLifecycleListener != null) {
                        mLifecycleListener.onBackToDesktop();
                    }
                }
            }
        }, delay);

    }

    @Override
    public void onActivityStarted(Activity activity) {
        startCount++;
    }


    @Override
    public void onActivityStopped(Activity activity) {
        startCount--;
        if (startCount == 0) {
            if (mLifecycleListener != null) {
                mLifecycleListener.onBackToDesktop();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                if (mLifecycleListener != null) {
                    mLifecycleListener.onBackToDesktop();
                }
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void unRegisterReceiver(Context context) {
        ((Application) context).unregisterActivityLifecycleCallbacks(this);
        context.unregisterReceiver(this);
    }

}
