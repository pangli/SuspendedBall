package com.zorro.suspendedball.widget.interfaces;

/**
 * @Copyright © 2017 Analysys Inc. All rights reserved.
 * @Description:
 * @Version: 1.0.9
 * @Create: 2017-11-14 17:15:35
 * @Author: yhao
 */
public abstract class BaseFloatView {
    public abstract void setGravity(int gravity, int xOffset, int yOffset);

    public abstract void init();

    public abstract void show();

    public abstract void hide();

    public abstract void dismiss();

    public abstract void destroy();
}
