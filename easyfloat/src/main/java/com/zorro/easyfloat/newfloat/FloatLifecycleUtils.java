package com.zorro.easyfloat.newfloat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Package:   com.zorro.easyfloat.newfloat
 * ClassName: FloatLifecycleUtils
 * Created by Zorro on 2020/5/18 18:21
 * 备注：通过生命周期回调，判断系统浮窗的信息，以及app是否位于前台
 */
public class FloatLifecycleUtils {
    private static Application application;
    private static Application.ActivityLifecycleCallbacks lifecycleCallbacks;
    private static ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private static WeakReference<Activity> mWeakAct;

    public static Activity getCurrentActivity() {
        if (mWeakAct != null && mWeakAct.get() != null && !(mWeakAct.get()).isFinishing()) {
            return mWeakAct.get();
        }
        return null;
    }

    public static void setLifecycleCallbacks(final Application application, final FloatingConfig config) {
        if (application != null) {
            FloatLifecycleUtils.application = application;
            lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
                public void onActivityCreated(@Nullable Activity activity, @Nullable Bundle savedInstanceState) {
                }

                public void onActivityStarted(@Nullable Activity activity) {

                }

                // 每次都要判断当前页面是否需要显示
                public void onActivityResumed(@Nullable Activity activity) {
                    if (activity != null) {
                        mWeakAct = new WeakReference<>(activity);
                        if (config != null && config.getFilterSet() != null) {
                            if (!config.getFilterSet().contains(activity.getComponentName().getClassName())) {
                                ((ViewGroup) activity.findViewById(android.R.id.content)).addView(FloatingView.getInstance(application), mParams);
                            }
                        } else {
                            ((ViewGroup) activity.findViewById(android.R.id.content)).addView(FloatingView.getInstance(application), mParams);
                        }

                    }
                }

                // 每次都要判断当前页面是否需要显示
                public void onActivityPaused(@Nullable Activity activity) {
                    if (activity != null) {
                        if (config != null && config.getFilterSet() != null) {
                            if (!config.getFilterSet().contains(activity.getComponentName().getClassName())) {
                                ((ViewGroup) activity.findViewById(android.R.id.content)).removeView(FloatingView.getInstance(application));
                            }
                        } else {
                            ((ViewGroup) activity.findViewById(android.R.id.content)).removeView(FloatingView.getInstance(application));
                        }
                    }
                }

                public void onActivityStopped(@Nullable Activity activity) {


                }

                public void onActivityDestroyed(@Nullable Activity activity) {
                }

                public void onActivitySaveInstanceState(@Nullable Activity activity, @Nullable Bundle outState) {
                }
            };
            application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
        } else {
            Log.e("LifecycleUtils", "application is null");
        }

    }


    public static void release() {
        if (application != null && lifecycleCallbacks != null) {
            FloatingView.getInstance(application).destroy();
            application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
            lifecycleCallbacks = null;
            mParams = null;
            application = null;
        } else {
            Log.e("LifecycleUtils", "application is null");
        }
    }
}
