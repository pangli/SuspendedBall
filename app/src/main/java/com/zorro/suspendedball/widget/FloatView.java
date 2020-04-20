package com.zorro.suspendedball.widget;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zorro.suspendedball.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Package:   com.zorro.suspendedball.widget
 * ClassName: FloatView
 * Created by Zorro on 2020/4/17 19:13
 * 备注： 悬浮球View
 */

public class FloatView extends FrameLayout {
    private static final String TAG = "FloatCallView";
    // 宽高比例
    private static float ratio = (float) 9 / 16;

    private Context mContext;


    private int resId;
    // View中X坐标
    private float xInView;
    // View中Y坐标
    private float yInView;
    // 当前X坐标
    private float xInScreen;
    // 当前Y坐标
    private float yInScreen;
    // 记录移动前X坐标
    private float mStartX;
    // 记录移动前Y坐标
    private float mStartY;

    private SurfaceView m_surfaceLocal;

    private OnClickListener mOnClickListner;

    private View mRootView;
    // 时间状态显示
    private TextView timeStateText;
    // 通过结束提示
    private TextView finishStateText;

    private WindowManager wm;

    private WindowManager.LayoutParams wmParams;

    private boolean m_bReconnecting = false;
    private int m_nRetries = 0;
    // 浮层是否正在销毁
    private boolean viewIsDestroying;

    private final int HANDLER_TYPE_HIDE_LOGO = 100;//隐藏LOGO
    private final int HANDLER_TYPE_CANCEL_ANIM = 101;//退出动画

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;

    private FrameLayout flContainer;
    private ImageView ivFloatView;


    private boolean mIsRight;//logo是否在右边
    private boolean mCanHide;//是否允许隐藏
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mDraging;
    private boolean mShowLoader = true;
    private boolean mShowState = false;

    private Timer mTimer;
    private TimerTask mTimerTask;

    @SuppressLint("HandlerLeak")
    final Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                // 比如隐藏悬浮框
                if (mCanHide) {
                    mCanHide = false;
                    if (mIsRight) {
                        ivFloatView.setImageResource(R.drawable.cml_image_float_right);
                    } else {
                        ivFloatView.setImageResource(R.drawable.cml_image_float_left);
                    }
                    ivFloatView.clearAnimation();
                    mWmParams.alpha = 0.7f;
//                    mWindowManager.updateViewLayout(FloatView.this, mWmParams);
                    refreshFloatMenu(mIsRight);
                }
            } else if (msg.what == HANDLER_TYPE_CANCEL_ANIM) {
//                mIvFloatLoader.clearAnimation();
//                mIvFloatLoader.setVisibility(View.GONE);
//                mShowLoader = false;
            }
            super.handleMessage(msg);
        }
    };


    public FloatView(Context context) {
        super(context);
        m_bReconnecting = false;
        m_nRetries = 0;
        this.mContext = context;
        resId = R.layout.cml_widget_float_view;
        View view = LayoutInflater.from(mContext).inflate(resId, this);
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mWindowManager =
                (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        createView(mContext);
        mTimer = new Timer();
    }


    /**
     * 创建Float view
     *
     * @param context
     * @return
     */
    private void createView(final Context context) {
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        ivFloatView = (ImageView) findViewById(R.id.iv_float_view);
    }


    public void setParams(WindowManager.LayoutParams params) {
        mWmParams = params;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = mWmParams.x;
        int oldY = mWmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
        }
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        removeTimerTask();
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                if (mShowState) {
                    ivFloatView.setImageResource(R.drawable.cml_icon_loading);
                    Animation rotaAnimation = AnimationUtils.loadAnimation(mContext,
                            R.anim.cml_anim_loading);
                    rotaAnimation.setInterpolator(new LinearInterpolator());
                    ivFloatView.startAnimation(rotaAnimation);
                } else {
                    ivFloatView.setImageResource(R.drawable.cml_icon_fail);
                    ivFloatView.clearAnimation();
                }
                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {

                    mDraging = true;
                    // 更新浮动窗口位置参数
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY);
                    mWindowManager.updateViewLayout(this, mWmParams);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mOnClickListner != null && !mDraging) {
                    mOnClickListner.onClick(this);
                }

            case MotionEvent.ACTION_CANCEL:
                if (mWmParams.x >= mScreenWidth / 2) {
                    // mWmParams.x = mScreenWidth;
                    mIsRight = true;
                } else if (mWmParams.x < mScreenWidth / 2) {
                    mIsRight = false;
                    // mWmParams.x = 0;
                }
                if (mShowState) {
                    ivFloatView.setImageResource(R.drawable.cml_icon_loading);
                    Animation rotaAnimation = AnimationUtils.loadAnimation(mContext,
                            R.anim.cml_anim_loading);
                    rotaAnimation.setInterpolator(new LinearInterpolator());
                    ivFloatView.startAnimation(rotaAnimation);
                } else {
                    ivFloatView.setImageResource(R.drawable.cml_icon_fail);
                   // mIvFloatLogo.clearAnimation();
                }
                final ValueAnimator animator = ValueAnimator.ofInt(mWmParams.x, mIsRight ?
                        mScreenWidth : 0);
                animator.setInterpolator(new BounceInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mWmParams.x = (int) animation.getAnimatedValue();
                        mWindowManager.updateViewLayout(FloatView.this, mWmParams);
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
                        refreshFloatMenu(mIsRight);
                        timerForHide();
//                        // 初始化
//                        mTouchStartX = mTouchStartY = 0;
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


    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
            ex.printStackTrace();
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
     * 显示悬浮窗
     */
    public void show() {
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
            if (mShowState) {
                ivFloatView.setImageResource(R.drawable.cml_icon_loading);
                Animation rotaAnimation = AnimationUtils.loadAnimation(mContext,
                        R.anim.cml_anim_loading);
                rotaAnimation.setInterpolator(new LinearInterpolator());
                ivFloatView.startAnimation(rotaAnimation);
            } else {
                ivFloatView.setImageResource(R.drawable.cml_icon_fail);
                ivFloatView.clearAnimation();
            }
            mWmParams.alpha = 1f;
            mWindowManager.updateViewLayout(this, mWmParams);


            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mTimerHandler.sendEmptyMessage(HANDLER_TYPE_CANCEL_ANIM);
                }
            }, 3000);

        }
    }

    /**
     * 刷新float view menu
     *
     * @param right
     */
    private void refreshFloatMenu(boolean right) {
        if (right) {
            FrameLayout.LayoutParams paramsFloatImage =
                    (FrameLayout.LayoutParams) ivFloatView.getLayoutParams();
            paramsFloatImage.gravity = Gravity.RIGHT;
            ivFloatView.setLayoutParams(paramsFloatImage);
            FrameLayout.LayoutParams paramsFlFloat =
                    (FrameLayout.LayoutParams) flContainer.getLayoutParams();
            paramsFlFloat.gravity = Gravity.RIGHT;
            flContainer.setLayoutParams(paramsFlFloat);
        } else {
            FrameLayout.LayoutParams params =
                    (FrameLayout.LayoutParams) ivFloatView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.LEFT;
            ivFloatView.setLayoutParams(params);
            FrameLayout.LayoutParams paramsFlFloat =
                    (FrameLayout.LayoutParams) flContainer.getLayoutParams();
            paramsFlFloat.gravity = Gravity.LEFT;
            flContainer.setLayoutParams(paramsFlFloat);
        }
    }

    /**
     * 定时隐藏float view
     */
    private void timerForHide() {
        mCanHide = true;
        //结束任务
        if (mTimerTask != null) {
            try {
                mTimerTask.cancel();
                mTimerTask = null;
            } catch (Exception e) {
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
     * 是否Float view
     */
    public void destroy() {
        hide();
        removeFloatView();
        removeTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        try {
            mTimerHandler.removeMessages(1);
        } catch (Exception e) {
        }
    }


    public void setState(boolean b) {
        mShowState = b;
        if (mShowState) {
            ivFloatView.setImageResource(R.drawable.cml_icon_loading);
            Animation rotaAnimation = AnimationUtils.loadAnimation(mContext,
                    R.anim.cml_anim_loading);
            rotaAnimation.setInterpolator(new LinearInterpolator());
            ivFloatView.startAnimation(rotaAnimation);
        } else {
            ivFloatView.setImageResource(R.drawable.cml_icon_fail);
            ivFloatView.clearAnimation();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListner = l;

    }
}