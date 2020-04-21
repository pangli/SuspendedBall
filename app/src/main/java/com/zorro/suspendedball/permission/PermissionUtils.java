package com.zorro.suspendedball.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.zorro.suspendedball.permission.room.HuaweiUtils;
import com.zorro.suspendedball.permission.room.MeizuUtils;
import com.zorro.suspendedball.permission.room.MiuiUtils;
import com.zorro.suspendedball.permission.room.OppoUtils;
import com.zorro.suspendedball.permission.room.QikuUtils;
import com.zorro.suspendedball.permission.room.RomUtils;

import java.lang.reflect.Field;

/**
 * Package:   com.zorro.suspendedball.permission
 * ClassName: PermissionUtils
 * Created by Zorro on 2020/4/21 15:00
 * 备注：悬浮窗权限工具类
 */
public class PermissionUtils {
    public static final int requestCode = 199;
    private static final String TAG = "PermissionUtils--->";

    /**
     * 检测是否有悬浮窗权限
     * 6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
     */
    public static boolean checkPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? (RomUtils.checkIsHuaweiRom() ? huaweiPermissionCheck(context) : (RomUtils.checkIsMiuiRom() ? miuiPermissionCheck(context) : (RomUtils.checkIsOppoRom() ? oppoROMPermissionCheck(context) : (RomUtils.checkIsMeizuRom() ? meizuPermissionCheck(context) : (RomUtils.checkIs360Rom() ? qikuPermissionCheck(context) : true))))) : commonROMPermissionCheck(context);
    }

    /**
     * 申请悬浮窗权限
     */
    public static void requestPermission(Activity activity, OnPermissionResult onPermissionResult) {
        PermissionFragment.requestPermission(activity, onPermissionResult);
    }

    public static void requestPermission(Fragment fragment) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsHuaweiRom()) {
                HuaweiUtils.applyPermission(fragment.getActivity());
            } else if (RomUtils.checkIsMiuiRom()) {
                MiuiUtils.applyMiuiPermission(fragment.getActivity());
            } else if (RomUtils.checkIsOppoRom()) {
                OppoUtils.applyOppoPermission(fragment.getActivity());
            } else if (RomUtils.checkIsMeizuRom()) {
                MeizuUtils.applyPermission(fragment);
            } else if (RomUtils.checkIs360Rom()) {
                QikuUtils.applyPermission(fragment.getActivity());
            } else {
                Log.i(TAG, "原生 Android 6.0 以下无需权限申请");
            }
        } else {
            commonROMPermissionApply(fragment);
        }

    }

    private static boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private static boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private static boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private static boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private static boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    /**
     * 6.0以后，通用悬浮窗权限检测
     * 但是魅族6.0的系统这种方式不好用，需要单独适配一下
     */
    private static boolean commonROMPermissionCheck(Context context) {
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    return Settings.canDrawOverlays(context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return true;
        }
    }

    /**
     * 通用 rom 权限申请
     */
    private static void commonROMPermissionApply(Fragment fragment) {
        // 这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            MeizuUtils.applyPermission(fragment);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                commonROMPermissionApplyInternal(fragment);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        } else {
            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
        }
    }

    public static void commonROMPermissionApplyInternal(Fragment fragment) {
        try {
            Field field = Settings.class.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            StringBuilder builder = new StringBuilder("package:");
            builder.append(fragment.getActivity().getPackageName());
            intent.setData(Uri.parse(builder.toString()));
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }

}
