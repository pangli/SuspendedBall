package com.zorro.suspendedball;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.View;

import com.zorro.suspendedball.config.FloatConfig;
import com.zorro.suspendedball.enums.ShowPattern;
import com.zorro.suspendedball.permission.OnPermissionResult;
import com.zorro.suspendedball.permission.PermissionUtils;
import com.zorro.suspendedball.utils.LifecycleUtils;
import com.zorro.suspendedball.widget.impl.AppFloatManager;
import com.zorro.suspendedball.widget.impl.FloatManager;
import com.zorro.suspendedball.widget.interfaces.OnFloatViewClick;

/**
 * Package:   com.zorro.suspendedball
 * ClassName: EasyFloat
 * Created by Zorro on 2020/4/21 15:04
 * 备注：
 */
public class EasyFloat {

    public static void init(Application application) {
        // 注册Activity生命周期回调
        LifecycleUtils.setLifecycleCallbacks(application);
    }

    public static EasyFloat.Builder with(Activity activity) {
        return new Builder(activity);
    }

    // *************************** Activity浮窗的相关方法 ***************************
    // 通过浮窗管理类，实现系统浮窗的相应的功能，详情参考FloatManager

    /**
     * 获取系统浮窗的config
     */
    private static FloatConfig getConfig(String tag) {
        AppFloatManager floatManager = FloatManager.getAppFloatManager(tag);
        if (floatManager != null) {
            return floatManager.getConfig();
        } else {
            return null;
        }
    }


    /**
     * 关闭系统级浮窗
     */
    public static void dismissAppFloat(String tag) {
        FloatManager.dismiss(tag);
    }

    /**
     * 隐藏系统浮窗
     */
    public static void hideAppFloat(String tag) {
        FloatManager.visible(false, tag, false);
    }


    /**
     * 显示系统浮窗
     */
    public static void showAppFloat(String tag) {
        FloatManager.visible(true, tag, true);
    }


    /**
     * 获取系统浮窗是否显示，通过浮窗的config，获取显示状态
     */
    public static boolean appFloatIsShow(String tag) {
        FloatConfig config = getConfig(tag);
        return config != null && config.isShow();
    }


    /**
     * 获取系统浮窗中，我们传入的View
     */
    public static View getAppFloatView(String tag) {
        FloatConfig config = getConfig(tag);
        return config != null ? config.getLayoutView() : null;
    }

    /**
     * 浮窗的属性构建类，支持链式调用
     */
    public static class Builder implements OnPermissionResult {
        private FloatConfig config;
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
            this.config = new FloatConfig();
        }


        public Builder setShowPattern(ShowPattern showPattern) {
            config.setShowPattern(showPattern);
            return this;
        }


        public Builder setTag(String floatTag) {
            config.setFloatTag(floatTag);
            return this;
        }


        /**
         * 通过传统接口，进行浮窗的各种状态回调
         */
        public Builder registerCallbacks(OnFloatViewClick callbacks) {
            config.setCallbacks(callbacks);
            return this;
        }

        /**
         * 创建浮窗，如若系统浮窗无权限，先进行权限申请
         */
        public void show() {
            if (PermissionUtils.checkPermission(activity)) {
                createAppFloat();
            } else {
                PermissionUtils.requestPermission(activity, this);
            }
        }

        /**
         * 创建系统浮窗
         */
        private void createAppFloat() {
            FloatManager.create(activity, config);
        }

        /**
         * 申请浮窗权限的结果回调
         */
        @Override
        public void permissionResult(boolean isOpen) {
            if (isOpen) {
                createAppFloat();
            } else {
                if (config.getCallbacks() != null) {
                    // config.getCallbacks().createdResult(false, "系统浮窗权限不足，开启失败", (View) null);
                }
                Log.e("Soin", "系统浮窗权限不足，开启失败");
            }
        }
    }

}
