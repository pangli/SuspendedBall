package com.zorro.suspendedball;

import android.app.Application;

/**
 * Package:   com.lzf.easyfloat.example
 * ClassName: App
 * Created by Zorro on 2020/4/17 14:32.
 * 备注：
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyFloat.init(this);
    }
}
