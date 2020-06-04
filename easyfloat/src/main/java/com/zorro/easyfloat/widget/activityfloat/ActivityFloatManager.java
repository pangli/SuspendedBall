package com.zorro.easyfloat.widget.activityfloat;

import android.app.Application;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Package:   com.zorro.easyfloat.widget.activityfloat
 * ClassName: ActivityFloatManager
 * Created by Zorro on 2020/6/4 15:00
 * 备注：  ActivityFloatManager 操作类
 */
public class ActivityFloatManager {
    private static WeakReference<ActivityFloatView> floatView;

    public static ActivityFloatView create(Application application) {
        if (floatView == null || floatView.get() == null) {
            synchronized (ActivityFloatManager.class) {
                if (floatView == null || floatView.get() == null) {
                    floatView = new WeakReference<>(new ActivityFloatView(application));
                }
            }
        }
        return floatView.get();
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
    }
}
