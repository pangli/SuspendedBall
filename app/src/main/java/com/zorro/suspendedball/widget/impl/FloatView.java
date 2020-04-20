package com.zorro.suspendedball.widget.impl;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zorro.suspendedball.R;
import com.zorro.suspendedball.widget.interfaces.OnFloatCallbacks;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Package:   com.zorro.suspendedball.widget
 * ClassName: FloatView
 * Created by Zorro on 2020/4/17 19:13
 * 备注： 悬浮球View
 */

public class FloatView extends FrameLayout {
    private final int HANDLER_TYPE_HIDE_LOGO = 100;//隐藏LOGO
    private Context mContext;
    //
    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private OnFloatCallbacks onFloatCallbacks;
    //
    private FrameLayout flContainer;
    private ImageView ivFloatView;

    //
    private boolean isRight = true;//logo是否在右边靠右
    private boolean mCanHide;//是否允许隐藏
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isDrag;


    private Timer mTimer;
    private TimerTask mTimerTask;

    @SuppressLint("HandlerLeak")
    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                // 比如隐藏悬浮框
                if (mCanHide) {
                    mCanHide = false;
                    if (isRight) {
                        ivFloatView.setImageResource(R.drawable.cml_image_float_right);
                    } else {
                        ivFloatView.setImageResource(R.drawable.cml_image_float_left);
                    }
                    refreshFloatViewGravity(isRight);
                    wmParams.alpha = 0.7f;
                }
            }
            super.handleMessage(msg);
        }
    };

    public FloatView(@NonNull Context context) {
        this(context, null);
    }

    public FloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.cml_widget_float_view, this);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        ivFloatView = (ImageView) findViewById(R.id.iv_float_view);
        view.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        //获取屏幕信息
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mTimer = new Timer();
        //处理按钮自动隐藏
        refreshFloatViewGravity(isRight);
        startTimerForHide();
    }

    public void setWindowMangerLayoutParams(WindowManager.LayoutParams params) {
        this.wmParams = params;
    }

    public void setOnFloatCallbacks(OnFloatCallbacks onFloatCallbacks) {
        this.onFloatCallbacks = onFloatCallbacks;
    }

    /**
     * 横竖屏切换
     *
     * @param newConfig
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = wmParams.x;
        int oldY = wmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                if (isRight) {
                    wmParams.x = mScreenWidth;
                    wmParams.y = oldY;
                } else {
                    wmParams.x = oldX;
                    wmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                if (isRight) {
                    wmParams.x = mScreenWidth;
                    wmParams.y = oldY;
                } else {
                    wmParams.x = oldX;
                    wmParams.y = oldY;
                }
                break;
        }
        if (onFloatCallbacks != null) {
            onFloatCallbacks.updateLayoutParams(wmParams);
        }
        // mWindowManager.updateViewLayout(this, wmParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        removeTimerTask();
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                //更新UI
                ivFloatView.setImageResource(R.drawable.cml_icon_fail);
                wmParams.alpha = 1f;
                if (onFloatCallbacks != null) {
                    onFloatCallbacks.updateLayoutParams(wmParams);
                }
                // mWindowManager.updateViewLayout(this, wmParams);

                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    isDrag = true;
                    // 更新浮动窗口位置参数
                    wmParams.x = (int) (x - mTouchStartX);
                    wmParams.y = (int) (y - mTouchStartY);
                    if (onFloatCallbacks != null) {
                        onFloatCallbacks.updateLayoutParams(wmParams);
                    }
                    // mWindowManager.updateViewLayout(this, wmParams);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (onFloatCallbacks != null && !isDrag) {
                    onFloatCallbacks.onClick(this);
                }
                isDrag = false;
            case MotionEvent.ACTION_CANCEL:
                isDrag = false;
                if (wmParams.x >= mScreenWidth / 2) {
                    isRight = true;
                } else if (wmParams.x < mScreenWidth / 2) {
                    isRight = false;
                }
                final ValueAnimator animator = ValueAnimator.ofInt(wmParams.x, isRight ?
                        mScreenWidth : 0);
//                animator.setInterpolator(new BounceInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        wmParams.x = (int) animation.getAnimatedValue();
                        if (onFloatCallbacks != null) {
                            onFloatCallbacks.updateLayoutParams(wmParams);
                        }
                        // mWindowManager.updateViewLayout(FloatView.this, wmParams);
                    }
                });
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator.removeAllUpdateListeners();
                        animator.removeAllListeners();
                        refreshFloatViewGravity(isRight);
                        startTimerForHide();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.setDuration(500).start();
//                refreshFloatMenu(mIsRight);
//                timerForHide();
//                mWindowManager.updateViewLayout(this, mWmParams);
//                // 初始化
//                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return false;
    }

    /**
     * 显示悬浮窗
     */
    public void show() {
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
            ivFloatView.setImageResource(R.drawable.cml_icon_fail);
            wmParams.alpha = 1f;
            if (onFloatCallbacks != null) {
                onFloatCallbacks.updateLayoutParams(wmParams);
            }
            //处理按钮自动隐藏
            removeTimerTask();
            refreshFloatViewGravity(isRight);
            startTimerForHide();
            // mWindowManager.updateViewLayout(this, wmParams);
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hide() {
        setVisibility(View.GONE);
        Message message = mTimerHandler.obtainMessage();
        message.what = HANDLER_TYPE_HIDE_LOGO;
        mTimerHandler.sendMessage(message);
        removeTimerTask();
    }

    /**
     * 刷新float view 的 Gravity
     *
     * @param right
     */
    private void refreshFloatViewGravity(boolean right) {
        if (right) {
            LayoutParams paramsFloatImage = (LayoutParams) ivFloatView.getLayoutParams();
            paramsFloatImage.gravity = Gravity.END;
            ivFloatView.setLayoutParams(paramsFloatImage);
            LayoutParams paramsFlFloat = (LayoutParams) flContainer.getLayoutParams();
            paramsFlFloat.gravity = Gravity.END;
            flContainer.setLayoutParams(paramsFlFloat);
        } else {
            LayoutParams params = (LayoutParams) ivFloatView.getLayoutParams();
            //params.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.START;
            ivFloatView.setLayoutParams(params);
            LayoutParams paramsFlFloat = (LayoutParams) flContainer.getLayoutParams();
            paramsFlFloat.gravity = Gravity.START;
            flContainer.setLayoutParams(paramsFlFloat);
        }
    }

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
     * 资源回收Float view
     */
    public void destroy() {
        hide();
        removeTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        try {
            if (mTimerHandler != null) {
                mTimerHandler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}