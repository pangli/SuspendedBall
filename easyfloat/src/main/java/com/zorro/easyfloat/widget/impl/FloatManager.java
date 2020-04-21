package com.zorro.easyfloat.widget.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zorro.easyfloat.config.FloatConfig;

import java.util.HashMap;

/**
 * Package:   com.zorro.easyfloat.widget.impl
 * ClassName: FloatManager
 * Created by Zorro on 2020/4/20 11:28.
 * 备注：系统浮窗的集合管理类，通过浮窗tag管理各个浮窗
 */
public class FloatManager {
    private static final String DEFAULT_TAG = "default";


    private static HashMap<String, AppFloatManager> floatMap = new HashMap<>();

    public static void create(Context context, FloatConfig config) {
        if (checkTag(config)) {
            AppFloatManager manager = new AppFloatManager(context.getApplicationContext(), config);
            manager.createFloat();
            floatMap.put(getTag(config.getFloatTag()), manager);
        } else {
            Log.e("Sino", "请为系统浮窗设置不同的tag");
        }
    }

    /**
     * 设置浮窗的显隐，用户主动调用隐藏时，needShow需要为false
     */
    public static void visible(boolean isShow, String tag, boolean needShow) {
        AppFloatManager manager = floatMap.get(getTag(tag));
        if (manager != null) {
            manager.setVisible(isShow ? View.VISIBLE : View.GONE, needShow);
        }
    }

    public static void dismiss(String tag) {
        AppFloatManager manager = floatMap.get(getTag(tag));
        if (manager != null) {
            manager.dismiss();
            floatMap.remove(getTag(tag));
        }
    }

    /**
     * 获取浮窗tag，为空则使用默认值
     */
    private static String getTag(String tag) {
        return TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag;
    }

    /**
     * 检测浮窗的tag是否有效，不同的浮窗必须设置不同的tag
     */
    private static boolean checkTag(FloatConfig config) {
        String tag = config.getFloatTag();
        config.setFloatTag(getTag(tag));
        return !floatMap.containsKey(getTag(tag));
    }

    public static HashMap<String, AppFloatManager> getFloatMap() {
        return floatMap;
    }

    /**
     * 获取具体的系统浮窗管理类
     */
    public static AppFloatManager getAppFloatManager(String tag) {
        return floatMap.get(getTag(tag));
    }
}
