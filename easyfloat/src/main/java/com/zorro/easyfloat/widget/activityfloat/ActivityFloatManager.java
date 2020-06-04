package com.zorro.easyfloat.widget.activityfloat;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Package:   com.zorro.easyfloat.widget.activityfloat
 * ClassName: ActivityFloatManager
 * Created by Zorro on 2020/6/4 15:00
 * 备注：  ActivityFloatManager 操作类
 */
public class ActivityFloatManager {
    private static WeakReference<FrameLayout> frameLayoutWeak;
    private static WeakReference<ActivityFloatView> floatView;
    private static ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public static FrameLayout create(Application application) {
        if (frameLayoutWeak == null || frameLayoutWeak.get() == null || floatView == null || floatView.get() == null) {
            synchronized (ActivityFloatManager.class) {
                if (frameLayoutWeak == null || frameLayoutWeak.get() == null || floatView == null || floatView.get() == null) {
                    FrameLayout frameLayout = new FrameLayout(application);
                    frameLayout.setBackgroundColor(application.getResources().getColor(android.R.color.transparent));
                    floatView = new WeakReference<>(new ActivityFloatView(application));
                    frameLayout.addView(floatView.get(), mParams);
                    frameLayoutWeak = new WeakReference<>(frameLayout);
                }
            }
        }
        return frameLayoutWeak.get();
    }


    public static void setVisibility(boolean isShow) {
        if (floatView == null || floatView.get() == null) return;
        floatView.get().setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public static void setOnViewClickListener(View.OnClickListener onClickListener) {
        if (floatView == null || floatView.get() == null) return;
        floatView.get().setOnClickListener(onClickListener);
    }

    public static void destroy() {
        if (floatView == null || floatView.get() == null) return;
        floatView.get().destroy();
        floatView.clear();
        if (frameLayoutWeak == null || frameLayoutWeak.get() == null) return;
        frameLayoutWeak.clear();
    }
}
