package com.demo.view.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * description：波浪
 * <p/>
 * Created by TIAN FENG on 2017/11/17
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class WaveView extends View {
    private Paint mWavePaint;
    private Path mPath;
    private int mRange = 40;
    private int mDx;// 横轴到一个顶点的横坐标距离   0019C0EA
    private int mStartX, mStartY;

    private Handler mHandler = new Handler();


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint.setDither(true);
        mWavePaint.setColor(Color.BLUE);
        mWavePaint.setStrokeWidth(30);
        mWavePaint.setStyle(Paint.Style.FILL);


        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * 一个周期需要4个 mDx ，屏幕上面画两个周期
         */
        mDx = getWidth() / 8;

        // 终点位置为控件的中心
        mStartY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        initPath();
        // draw
        canvas.drawPath(mPath, mWavePaint);
    }

    /**
     * 初始化路径
     */
    private void initPath() {
        // 移动到起点
        mPath.moveTo(mStartX, mStartY);
        // 先画整个周期
        for (int i = 1; i <=8; i++) {
            if (i % 2 == 1) {
                // 控制点在起点上方一个mDx
                mPath.quadTo((i * 2 - 1) * mDx+mStartX, mStartY + mRange, 2 * i * mDx+mStartX, mStartY);
            } else {
                // 控制点在起点下方一个mDx
                mPath.quadTo((i * 2 - 1) * mDx+mStartX, mStartY - mRange, 2 * i * mDx+mStartX, mStartY);
            }
        }
        // 闭合整个控件
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(mStartX, getHeight());
        mPath.lineTo(mStartX, mStartY);
        mPath.close();
    }

    /**
     * 设置起点坐标的Y轴高度 rangle  0~1
     */
    public void setStartYRange(float rangle){
        if(rangle<0|rangle>1){
            throw new IllegalArgumentException("rangle mast in 0-1.");
        }
        mStartY = (int) (getHeight()*rangle);
        invalidate();
    }

    private float fraction = 0f;

    public void startAnimation(){
        // 开启动画
//        final ValueAnimator animator = ValueAnimator.ofFloat(0, 4);// 一个周期
//        animator.setDuration(1000);// 只需时间
//        animator.setRepeatCount(ValueAnimator.INFINITE);// 循环重复
//        animator.setInterpolator(new LinearInterpolator());// 先行差值
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float fraction = (float) animation.getAnimatedValue();
//                mStartX = (int) (mDx * -fraction);// 更改起点值往左
//                postInvalidate();// 重绘
//            }
//        });
//        animator.start();

            fraction-=0.1;


        if (fraction<-4){
            fraction = 0;
        }
        mStartX = (int) (mDx * fraction);// 更改起点值往左
        postInvalidate();// 重绘
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        },5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
