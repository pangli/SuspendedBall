package com.zorro.suspendedball.config;

import android.view.View;

import com.zorro.suspendedball.enums.ShowPattern;
import com.zorro.suspendedball.widget.interfaces.OnFloatCallbacks;

/**
 * Package:   com.lzf.easyfloat.data
 * ClassName: FloatConfig
 * Created by Zorro on 2020/4/16 10:23.
 * 备注：
 */
public class FloatConfig {

    private View layoutView;
    // 当前浮窗的tag
    private String floatTag;
    // 是否显示
    private boolean isShow;

    // 浮窗显示类型（默认只在当前页显示）
    private ShowPattern showPattern = ShowPattern.FOREGROUND;


    private OnFloatCallbacks callbacks;
    // 是否需要显示，当过滤信息匹配上时，该值为false（用户手动调用隐藏，该值也为false，相当于手动过滤）
    private boolean needShow = true;


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

    public OnFloatCallbacks getCallbacks() {
        return this.callbacks;
    }

    public void setCallbacks(OnFloatCallbacks callbacks) {
        this.callbacks = callbacks;
    }


    public boolean getNeedShow() {
        return this.needShow;
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }


}
