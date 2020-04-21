package com.zorro.suspendedball.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.zorro.suspendedball.EasyFloat;
import com.zorro.suspendedball.R;
import com.zorro.suspendedball.enums.ShowPattern;
import com.zorro.suspendedball.permission.PermissionUtils;
import com.zorro.suspendedball.widget.interfaces.OnFloatViewClick;


public class MainActivity1 extends AppCompatActivity {
    //    private FloatService mFloatService;
//    private Boolean aBoolean = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowFloat = (Button) findViewById(R.id.btn_show_float);
        btnShowFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        Button btnHideFloat = (Button) findViewById(R.id.btn_hide_float);
        btnHideFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyFloat.hideAppFloat("boll");
            }
        });
        Button btnstateFloat = (Button) findViewById(R.id.btn_state_float);
        btnstateFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity1.this, OneActivity.class));
            }
        });


    }

    /**
     * 检测浮窗权限是否开启，若没有给与申请提示框（非必须，申请依旧是EasyFloat内部内保进行）
     */
    private void checkPermission() {
        if (PermissionUtils.checkPermission(this)) {
            showAppFloat();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                    .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAppFloat();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

        }
    }

    /**
     * 显示悬浮图标
     */
    public void showAppFloat() {
        EasyFloat.with(this).setTag("boll").setShowPattern(ShowPattern.FOREGROUND).registerCallbacks(new OnFloatViewClick() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity1.this, "我被点击了", Toast.LENGTH_LONG).show();
            }
        }).show();
    }


}
