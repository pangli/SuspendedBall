package com.zorro.suspendedball.widget.interfaces;

import android.view.View;
import android.view.WindowManager;

/**
 * @Copyright © 2017 Analysys Inc. All rights reserved.
 * @Description: https://github.com/yhaolpz
 * @Version: 1.0
 * @Create: 2017/12/22 17:05:41
 * @Author: yhao
 */
public interface OnFloatCallbacks {
    void updateLayoutParams(WindowManager.LayoutParams params);

    void onClick(View v);
}
