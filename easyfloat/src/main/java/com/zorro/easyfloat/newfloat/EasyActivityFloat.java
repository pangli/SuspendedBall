package com.zorro.easyfloat.newfloat;

import android.app.Application;
import android.view.View;

/**
 * Package:   com.zorro.suspendedball
 * ClassName: EasyFloat
 * Created by Zorro on 2020/4/21 15:04
 * 备注：
 */
public class EasyActivityFloat {

    public static void init(Application application, FloatingConfig config) {
        // 注册Activity生命周期回调
        FloatLifecycleUtils.setLifecycleCallbacks(application, config);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        FloatingView.setOnViewClickListener(onClickListener);
    }

    public static void show() {
        FloatingView.setVisibility(true);
    }

    public static void dismiss() {
        FloatingView.setVisibility(false);
    }

    public static void release() {
        // 资源回收
        FloatLifecycleUtils.release();
    }
}
