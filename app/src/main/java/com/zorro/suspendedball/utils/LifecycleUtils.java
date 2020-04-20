package com.zorro.suspendedball.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zorro.suspendedball.enums.ShowPattern;
import com.zorro.suspendedball.widget.impl.AppFloatManager;
import com.zorro.suspendedball.widget.impl.FloatManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Package:   com.lzf.easyfloat.utils
 * ClassName: LifecycleUtils
 * Created by Zorro on 2020/4/16 13:31.
 * 备注：通过生命周期回调，判断系统浮窗的过滤信息，以及app是否位于前台，通过广播通知浮窗service
 */
public class LifecycleUtils {
    private static int activityCount = 0;

    public static void setLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(@Nullable Activity activity, @Nullable Bundle savedInstanceState) {
            }

            public void onActivityStarted(@Nullable Activity activity) {
                // 计算启动的activity数目
                if (activity != null) activityCount++;

            }

            // 每次都要判断当前页面是否需要显示
            public void onActivityResumed(@Nullable Activity activity) {
                checkShow(activity);
            }

            public void onActivityPaused(@Nullable Activity activity) {
            }

            public void onActivityStopped(@Nullable Activity activity) {
                if (activity != null) {
                    // 计算关闭的activity数目，并判断当前App是否处于后台
                    activityCount--;
                    checkHide();
                }

            }

            public void onActivityDestroyed(@Nullable Activity activity) {
            }

            public void onActivitySaveInstanceState(@Nullable Activity activity, @Nullable Bundle outState) {
            }
        });
    }

    private static void checkShow(Activity activity) {
        if (activity != null) {
            HashMap<String, AppFloatManager> hashMap = FloatManager.getFloatMap();
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String tag = (String) entry.getKey();
                AppFloatManager manager = (AppFloatManager) entry.getValue();
                // 如果没有手动隐藏浮窗
                if (manager.getConfig().getNeedShow()) {
                    setVisible(true, tag);
                }
            }

        }
    }

    private static void checkHide() {
        if (!isForeground()) {
            HashMap<String, AppFloatManager> hashMap = FloatManager.getFloatMap();
            for (Map.Entry<String, AppFloatManager> entry : hashMap.entrySet()) {
                String tag = entry.getKey();
                AppFloatManager manager = entry.getValue();
                // 当app处于后台时，不是仅前台显示的浮窗，都需要显示
                setVisible(manager.getConfig().getShowPattern() != ShowPattern.FOREGROUND, tag);
            }

        }
    }

    private static boolean isForeground() {
        return activityCount > 0;
    }

    private static void setVisible(boolean isShow, String tag) {
        FloatManager.visible(isShow, tag, true);
    }


}
