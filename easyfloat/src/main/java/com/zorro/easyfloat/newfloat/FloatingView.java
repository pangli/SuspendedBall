package com.zorro.easyfloat.newfloat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;

import com.zorro.easyfloat.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Package:   com.zorro.easyfloat.newfloat
 * ClassName: FloatingView
 * Created by Zorro on 2020/5/18 16:22
 * 备注：可拖动的浮动按钮
 */
public class FloatingView extends RelativeLayout {

    @SuppressLint("StaticFieldLeak")
    private static FloatingView INSTANCE;
    private int mIconId;
    private boolean isRight = true;//logo是否在右边靠右
    private ImageView ivFloatView;
    private float oldY = 200;

    public static FloatingView getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (FloatingView.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FloatingView(application);
                }
            }
        }
        return INSTANCE;
    }

    public static void setVisibility(boolean isShow) {
        if (INSTANCE == null) return;
        INSTANCE.setVisibility(isShow ? VISIBLE : GONE);
    }

    public static void setOnViewClickListener(View.OnClickListener onClickListener) {
        if (INSTANCE == null) return;
        INSTANCE.setOnClickListener(onClickListener);
    }

    public FloatingView(Application application) {
        super(application);
        inflate(getContext(), R.layout.sino_widget_float_view, this);
        ivFloatView = findViewById(R.id.iv_float_view);
        mTimer = new Timer();
        TouchUtils.setOnTouchListener(this, new TouchUtils.OnTouchUtilsListener() {

            private int rootViewWidth;
            private int rootViewHeight;
            private int viewWidth;
            private int viewHeight;

            @Override
            public boolean onDown(View view, int x, int y, MotionEvent event) {
                getViewTreeObserver().removeOnDrawListener(onDrawListener);
                removeTimerTask();
                viewWidth = view.getWidth();
                viewHeight = view.getHeight();
                View contentView = view.getRootView().findViewById(android.R.id.content);
                rootViewWidth = contentView.getWidth();
                rootViewHeight = contentView.getHeight();
                ivFloatView.setImageResource(R.mipmap.gp_float_main_icon);
                ivFloatView.setAlpha(1f);
                processScale(view, true);
                return true;
            }

            @Override
            public boolean onMove(View view, int direction, int x, int y, int dx, int dy, int totalX, int totalY, MotionEvent event) {
                view.setX(Math.min(Math.max(0, view.getX() + dx), rootViewWidth - viewWidth));
                view.setY(Math.min(view.getY() + dy, rootViewHeight - viewHeight));
                return true;
            }

            @Override
            public boolean onStop(View view, int direction, int x, int y, int totalX, int totalY, int vx, int vy, MotionEvent event) {
                stick2HorizontalSide(view);
                processScale(view, false);
                return true;
            }

            private void stick2HorizontalSide(View view) {
                float endX = 0;
                if ((view.getX() + viewWidth / 2f) > (rootViewWidth / 2f)) {
                    isRight = true;
                    endX = rootViewWidth - viewWidth;
                } else {
                    isRight = false;
                    endX = 0;
                }
                view.animate()
//                        .setInterpolator(new DecelerateInterpolator())
                        .setInterpolator(new BounceInterpolator())
                        .translationX(endX)
                        .setDuration(500)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                startTimerForHide();
                            }
                        })
                        .start();
            }

            private void processScale(final View view, boolean isDown) {
                float value = isDown ? 1 - 0.1f : 1;
                view.animate()
                        .scaleX(value)
                        .scaleY(value)
                        .setDuration(100)
                        .start();
            }
        });
        setVisibility(GONE);
    }

    private ViewTreeObserver.OnDrawListener onDrawListener = new ViewTreeObserver.OnDrawListener() {
        @Override
        public void onDraw() {
            View contentView = getRootView().findViewById(android.R.id.content);
            if (contentView != null) {
                if (isRight) {
                    setX(contentView.getWidth() - getWidth());
                } else {
                    setX(0);
                }
                if (oldY > contentView.getHeight()) {
                    setY(contentView.getHeight() - getWidth());
                } else {
                    setY(oldY);
                }
            }
            startTimerForHide();
        }
    };


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnDrawListener(onDrawListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnDrawListener(onDrawListener);
        oldY = getY();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        View contentView = getRootView().findViewById(android.R.id.content);
        if (contentView != null) {
            if (isRight) {
                setX(contentView.getHeight() - getWidth());
            } else {
                setX(0);
            }
            float Y = contentView.getWidth() * getY() / contentView.getHeight();
            if ((Y + getHeight()) > contentView.getWidth()) {
                Y = contentView.getWidth() - getHeight();
            }
            setY(Y);
            switch (newConfig.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE://横屏
                    break;
                case Configuration.ORIENTATION_PORTRAIT://竖屏
                    break;
            }
            Log.d("FloatView", "屏幕getHeight----" + contentView.getHeight() + "-----getWidth-----" + contentView.getWidth());
        }

    }

    public void setIconId(@DrawableRes int icon) {
        ImageView ivFloatView = findViewById(R.id.iv_float_view);
        ivFloatView.setImageResource(mIconId);
    }

    public int getIconId() {
        return mIconId;
    }


    private Timer mTimer;
    private TimerTask mTimerTask;
    private final int HANDLER_TYPE_HIDE_LOGO = 100;//隐藏LOGO
    private boolean mCanHide;//是否允许隐藏
    @SuppressLint("HandlerLeak")
    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                // 比如隐藏悬浮框
                if (mCanHide) {
                    mCanHide = false;
                    if (isRight) {
                        ivFloatView.setImageResource(R.mipmap.gp_float_main_icon_half_rs);
                    } else {
                        ivFloatView.setImageResource(R.mipmap.gp_float_main_icon_half_ls);
                    }
                    refreshFloatViewGravity(isRight);
                    ivFloatView.setAlpha(0.7f);
                }
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 启动定时隐藏float view
     */
    private void startTimerForHide() {
        mCanHide = true;
        //结束任务
        if (mTimerTask != null) {
            try {
                mTimerTask.cancel();
                mTimerTask = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = mTimerHandler.obtainMessage();
                message.what = HANDLER_TYPE_HIDE_LOGO;
                mTimerHandler.sendMessage(message);
            }
        };
        if (mCanHide) {
            mTimer.schedule(mTimerTask, 6000, 3000);
        }
    }

    /**
     * 移除定时任务
     */
    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    /**
     * 刷新float view 的 Gravity
     *
     * @param right
     */
    private void refreshFloatViewGravity(boolean right) {
        if (right) {
            FrameLayout.LayoutParams paramsFloatImage = (FrameLayout.LayoutParams) ivFloatView.getLayoutParams();
            paramsFloatImage.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
            ivFloatView.setLayoutParams(paramsFloatImage);
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivFloatView.getLayoutParams();
            //params.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
            ivFloatView.setLayoutParams(params);
        }
    }

    /**
     * 资源回收Float view
     */
    public void destroy() {
        try {
            removeTimerTask();
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerHandler != null) {
                mTimerHandler.removeCallbacksAndMessages(null);
            }
            INSTANCE = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
