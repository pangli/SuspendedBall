package com.zorro.suspendedball;

import android.app.Application;

import com.zorro.easyfloat.EasyFloat;

/**
 * Package:   com.zorro.suspendedball
 * ClassName: App
 * Created by Zorro on 2020/4/21 15:04
 * 备注：
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyFloat.init(this);
//        LinkedHashSet<String> set=new LinkedHashSet<>();
//        set.add(TwoActivity.class.getCanonicalName());
//        set.add(FourActivity.class.getCanonicalName());
//        FloatingConfig config=new FloatingConfig();
//        config.setFilterSet(set);
//        FloatLifecycleUtils.setLifecycleCallbacks(this,config);
    }
}
