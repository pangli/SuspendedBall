package com.zorro.easyfloat.newfloat;

import java.util.LinkedHashSet;


/**
 * Package:   com.zorro.easyfloat.newfloat
 * ClassName: FloatingConfig
 * Created by Zorro on 2020/5/18 16:29
 * 备注：  悬浮球配置
 */
public class FloatingConfig {

    private LinkedHashSet<String> filterSet = new LinkedHashSet<String>();

    public LinkedHashSet<String> getFilterSet() {
        return filterSet;
    }

    public void setFilterSet(LinkedHashSet<String> filterSet) {
        this.filterSet = filterSet;
    }
}
