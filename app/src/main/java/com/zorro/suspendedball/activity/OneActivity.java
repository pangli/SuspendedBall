package com.zorro.suspendedball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zorro.easyfloat.newfloat.EasyActivityFloat;
import com.zorro.suspendedball.R;

/**
 * Created by lenovo on 2020/4/19.
 * 备注：
 */
public class OneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        TextView tv_flag = findViewById(R.id.tv_flag);
        tv_flag.setText("Activity One");
        findViewById(R.id.btn_start_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OneActivity.this, TwoActivity.class));
            }
        });
        EasyActivityFloat.show();
    }
}
