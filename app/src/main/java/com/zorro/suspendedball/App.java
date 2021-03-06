package com.zorro.suspendedball;

import android.app.Application;

import com.zorro.easyfloat.config.FloatConfig;
import com.zorro.easyfloat.widget.activityfloat.EasyActivityFloat;
import com.zorro.suspendedball.activity.FourActivity;
import com.zorro.suspendedball.activity.TwoActivity;

import java.util.LinkedHashSet;

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
//        EasyFloat.init(this);
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add(TwoActivity.class.getCanonicalName());
        set.add(FourActivity.class.getCanonicalName());
        FloatConfig config = new FloatConfig();
        config.setFilterSet(set);
        EasyActivityFloat.init(this, config);
    }
}
