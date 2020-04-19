package com.zorro.suspendedball.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zorro.suspendedball.R;
import com.zorro.suspendedball.permission.FloatActivity;
import com.zorro.suspendedball.permission.PermissionListener;
import com.zorro.suspendedball.permission.PermissionUtil;
import com.zorro.suspendedball.widget.impl.FloatPhone;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity1 extends AppCompatActivity {
    //    private FloatService mFloatService;
//    private Boolean aBoolean = false;
    private FloatPhone floatPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowFloat = (Button) findViewById(R.id.btn_show_float);
        btnShowFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissionUtil.hasPermission(MainActivity1.this)) {
                    showFloatPermissionDialog();
                } else {
//                    showFloatingView();
                    floatPhone.init();
                }
            }
        });

        Button btnHideFloat = (Button) findViewById(R.id.btn_hide_float);
        btnHideFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFloatingView();
            }
        });
        Button btnstateFloat = (Button) findViewById(R.id.btn_state_float);
        btnstateFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity1.this, OneActivity.class));
//                changState(aBoolean);
//                aBoolean = !aBoolean;
//                Toast.makeText(MainActivity.this, "hh", Toast.LENGTH_SHORT).show();

            }
        });

//        try {
//            Intent intent = new Intent(this, FloatService.class);
//            startService(intent);
//            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        floatPhone = new FloatPhone(this);


    }


    /**
     * 弹出是否确认退出的对话框
     */
    private void showFloatPermissionDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("请开启悬浮窗权限")
                .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        requestFloatPermission();
                        floatPhone.init();
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    //
    private void requestFloatPermission() {
        FloatActivity.request(this, new PermissionListener() {
            @Override
            public void onSuccess() {
                floatPhone.init();
            }

            @Override
            public void onFail() {

            }
        });
    }


    /**
     * 显示悬浮图标
     */
    public void showFloatingView() {
//        if (mFloatService != null) {
//            mFloatService.createFloatingWindow();
//        }
        if (floatPhone != null) {
            floatPhone.show();
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public void hideFloatingView() {
//        if (mFloatService != null) {
//            mFloatService.removeFloatView();
//        }
        if (floatPhone != null) {
            floatPhone.hide();
        }
    }
//
//    public void changState(boolean b) {
//        if (mFloatService != null) {
//            mFloatService.setState(b);
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        destroy();
    }

//    /**
//     * 释放PJSDK数据
//     */
//    public void destroy() {
//        try {
//            stopService(new Intent(this, FloatService.class));
//            unbindService(mServiceConnection);
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * 连接到Service
//     */
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            mFloatService = ((FloatService.FloatViewServiceBinder) iBinder).getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            mFloatService = null;
//        }
//    };
}
