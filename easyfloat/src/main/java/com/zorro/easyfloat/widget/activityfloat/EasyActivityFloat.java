package com.zorro.easyfloat.widget.activityfloat;

import android.app.Application;
import android.view.View;

import com.zorro.easyfloat.config.FloatConfig;

/**
 * Package:   com.zorro.easyfloat.widget.activityfloat
 * ClassName: EasyActivityFloat
 * Created by Zorro on 2020/6/4 15:02
 * 备注： ActivityFloat使用管理类
 */
public class EasyActivityFloat {


    public static void init(Application application, FloatConfig config) {
        // 注册Activity生命周期回调
        ActivityFloatLifecycleUtils.setLifecycleCallbacks(application, config);
    }
    // *************************** Activity悬浮View的相关方法 ***************************
    // 通过悬浮View管理类，实现Activity悬浮View的相应的功能，详情参考ActivityFloatManager


    /**
     * 回收Activity悬浮View
     */
    public static void destroyActivityFloat() {
        ActivityFloatManager.destroy();
    }

    /**
     * 隐藏Activity悬浮View
     */
    public static void hideActivityFloat() {
        ActivityFloatManager.setVisibility(false);
    }


    /**
     * 显示Activity悬浮View
     */
    public static void showActivityFloat() {
        ActivityFloatManager.setVisibility(true);
    }


    public static void setOnClickListener(View.OnClickListener onClickListener) {
        ActivityFloatManager.setOnViewClickListener(onClickListener);
    }


}
