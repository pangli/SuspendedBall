package com.zorro.easyfloat.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;


/**
 * Package:   com.zorro.easyfloat.permission
 * ClassName: PermissionFragment
 * Created by Zorro on 2020/4/21 15:00
 * 备注：用于浮窗权限的申请，自动处理回调结果
 */
public class PermissionFragment extends Fragment {
    private static final String TAG = "PermissionFragment--->";
    private static OnPermissionResult onPermissionResult;

    public static void requestPermission(Activity activity, OnPermissionResult onPermissionResult) {
        if (activity != null) {
            PermissionFragment.onPermissionResult = onPermissionResult;
            activity.getFragmentManager().beginTransaction()
                    .add(new PermissionFragment(), activity.getLocalClassName())
                    .commitAllowingStateLoss();
        } else {
            Log.e(TAG, "activity is null");
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PermissionUtils.requestPermission(this);
        Log.i(TAG, "PermissionFragment：requestPermission");
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PermissionUtils.requestCode) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public final void run() {
                    Activity activity = getActivity();
                    if (activity != null) {
                        boolean check = PermissionUtils.checkPermission(activity);
                        Log.i(TAG, "PermissionFragment onActivityResult: " + check);
                        // 回调权限结果
                        if (onPermissionResult != null) {
                            onPermissionResult.permissionResult(check);
                        }
                        // 将Fragment移除
                        getFragmentManager().beginTransaction()
                                .remove(PermissionFragment.this)
                                .commitAllowingStateLoss();
                    }
                }
            }, 500L);
        }

    }
}
