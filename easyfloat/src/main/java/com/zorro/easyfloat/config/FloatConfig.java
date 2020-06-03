package com.zorro.easyfloat.config;

import android.view.View;

import com.zorro.easyfloat.enums.ShowPattern;
import com.zorro.easyfloat.widget.interfaces.OnFloatViewClick;

import java.util.LinkedHashSet;


/**
 * Package:   com.zorro.easyfloat.config
 * ClassName: FloatConfig
 * Created by Zorro on 2020/4/21 14:59
 * 备注：   悬浮球配置
 */
public class FloatConfig {

    private View layoutView;
    // 当前浮窗的tag
    private String floatTag;
    // 是否显示
    private boolean isShow;

    // 浮窗显示类型（默认只在当前页显示）
    private ShowPattern showPattern = ShowPattern.FOREGROUND;


    private OnFloatViewClick callbacks;
    // 是否需要显示，当过滤信息匹配上时，该值为false（用户手动调用隐藏，该值也为false，相当于手动过滤）
    private boolean needShow = true;
    private LinkedHashSet<String> filterSet = new LinkedHashSet<String>();

    public View getLayoutView() {
        return this.layoutView;
    }

    public void setLayoutView(View layoutView) {
        this.layoutView = layoutView;
    }


    public String getFloatTag() {
        return this.floatTag;
    }

    public void setFloatTag(String floatTag) {
        this.floatTag = floatTag;
    }


    public boolean isShow() {
        return this.isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }


    public ShowPattern getShowPattern() {
        return this.showPattern;
    }

    public void setShowPattern(ShowPattern showPattern) {
        this.showPattern = showPattern;
    }

    public OnFloatViewClick getCallbacks() {
        return this.callbacks;
    }

    public void setCallbacks(OnFloatViewClick callbacks) {
        this.callbacks = callbacks;
    }


    public boolean getNeedShow() {
        return this.needShow;
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }

    public LinkedHashSet<String> getFilterSet() {
        return filterSet;
    }

    public void setFilterSet(LinkedHashSet<String> filterSet) {
        this.filterSet = filterSet;
    }

}
