package com.zorro.suspendedball.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zorro.easyfloat.EasyFloat;
import com.zorro.easyfloat.enums.ShowPattern;
import com.zorro.easyfloat.permission.PermissionUtils;
import com.zorro.easyfloat.widget.interfaces.OnFloatViewClick;
import com.zorro.suspendedball.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EasyFloat.init(this.getApplication());
        Button btnCreateShowFloat = findViewById(R.id.btn_create_show_float);
        btnCreateShowFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        Button btnShowFloat = findViewById(R.id.btn_show_float);
        btnShowFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyFloat.showAppFloat("boll");
            }
        });

        Button btnHideFloat = findViewById(R.id.btn_hide_float);
        btnHideFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyFloat.hideAppFloat("boll");
            }
        });
        Button btnCloseFloat = findViewById(R.id.btn_close_float);
        btnCloseFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyFloat.dismissAppFloat("boll");
            }
        });
        Button btnNextActivity = findViewById(R.id.btn_next_activity);
        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OneActivity.class));
            }
        });
    }

    /**
     * 检测浮窗权限是否开启，若没有给与申请提示框（非必须，申请依旧是EasyFloat内部内保进行）
     */
    private void checkPermission() {
        if (PermissionUtils.checkPermission(this.getApplicationContext())) {
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
                Toast.makeText(MainActivity.this, "我被点击了", Toast.LENGTH_LONG).show();
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        EasyFloat.dismissAppFloat("boll");
        super.onDestroy();
    }
}
