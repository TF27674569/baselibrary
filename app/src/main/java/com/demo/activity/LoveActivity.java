package com.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.demo.R;
import com.demo.base.NavigationTitle;

import org.annotation.Event;
import org.annotation.ViewById;
import org.base.BaseActivity;
import org.log.L;

import java.util.concurrent.Executors;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/7.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class LoveActivity extends BaseActivity {

    private int mTitleHeight;

    @ViewById(R.id.ivLover)
    public ImageView ivLover;
    private int[] mImgLoves = {R.drawable.pl_red, R.drawable.pl_blue, R.drawable.pl_yellow};


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_love);
    }

    @Override
    protected void initView() {
        mTitleHeight = ((NavigationTitle) new NavigationTitle.Builder(this)
                .setTitle("点赞")
                .setBackClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .immersed()
                .build()).getTitleHeight();

    }

    boolean flag = true, isRun = false;

    @Override
    protected void initData() {
        ivLover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flag = true;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flag = false;
                } else {
                    if (!isRun) {
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                while (flag) {
                                    isRun = true;
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            startAnima(ivLover);
                                        }
                                    });
                                }
                                isRun = false;
                            }
                        });
                    }
                }
                return true;
            }
        });
    }

    @Event(R.id.ivLover)
    public void ivLoverClick(ImageView ivLover) {
        startAnima(ivLover);
    }

    int pos = 0;

    // 执行动画
    private void startAnima(final ImageView ivLover) {
        final int[] posInWindow = new int[2];
        // 获取此点在屏幕中的位置
        ivLover.getLocationInWindow(posInWindow);
        // 创建一个View
        final ImageView imageView = new ImageView(this);
        // 随机一张图
        int position = pos % 3;
        pos++;
        imageView.setImageResource(mImgLoves[position]);
        ivLover.setImageResource(mImgLoves[position]);
        // 设置view参数
        imageView.setLayoutParams(ivLover.getLayoutParams());

        // 拿到根视图
        final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        decorView.addView(imageView);

        // 设置view的起点位置
        imageView.setX(posInWindow[0]);
        imageView.setY(posInWindow[1]);

        // 执行缩放动画
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageView, "scaleX",
                0f, 1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageView, "scaleY",
                0f, 1f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(ivLover, "scaleX",
                0f, 1f);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(ivLover, "scaleY",
                0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(100);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2, anim3, anim4);

        // 起点
        final PointF startPoint = new PointF(posInWindow[0], posInWindow[1]);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 启动贝塞尔动画
                startBezierAnima(startPoint, imageView, decorView);
            }
        });

        animSet.start();


    }

    // 启动贝塞尔动画
    private void startBezierAnima(PointF startPoint, final View view, final ViewGroup decorView) {
        // 终点为X = 起点X 的 左右150像素内的位置
        PointF endPointF = new PointF((float) (startPoint.x + (Math.random() * 600 - 300)), mTitleHeight);

        int random = (int) (Math.random() * 10);
        boolean type = random % 2 == 0;
        L.e(type);

        PointF controlPoint1 = getPointF(type, startPoint);
        PointF controlPoint2 = getPointF(!type, startPoint);


        // 贝塞尔动画
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(startPoint, endPointF), controlPoint1, controlPoint2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF currentPoint = (PointF) animation.getAnimatedValue();
                view.setX(currentPoint.x);
                view.setY(currentPoint.y);
            }
        });

        // 插值器
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                decorView.removeView(view);
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();

    }

    /**
     * 获取控制点
     */
    PointF getPointF(boolean type, PointF startPoint) {
        int x = (int) (type ? startPoint.x - Math.random() * 250 : startPoint.x + Math.random() * 250);
        int y = (int) (type ? startPoint.y / 4 * 3 : startPoint.y / 4);
        return new PointF(x, y);
    }


    // 估值器
    private class BezierEvaluator implements TypeEvaluator<PointF> {

        private PointF p0;// 起点
        private PointF p3;// 终点

        private BezierEvaluator(PointF p0, PointF p3) {
            super();
            this.p0 = p0;
            this.p3 = p3;
        }

        /**
         * @param fraction 0->1
         * @param p1       控制点1
         * @param p2       控制点2
         * @return 目前所在的点
         */
        @Override
        public PointF evaluate(float fraction, PointF p1, PointF p2) {
            PointF pointf = new PointF();
            // 贝塞尔曲线公式  p0*(1-t)^3 + 3p1*t*(1-t)^2 + 3p2*t^2*(1-t) + p3^3
            pointf.x = p0.x * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + 3 * p1.x * fraction * (1 - fraction) * (1 - fraction)
                    + 3 * p2.x * fraction * fraction * (1 - fraction)
                    + p3.x * fraction * fraction * fraction;
            pointf.y = p0.y * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + 3 * p1.y * fraction * (1 - fraction) * (1 - fraction)
                    + 3 * p2.y * fraction * fraction * (1 - fraction)
                    + p3.y * fraction * fraction * fraction;
            return pointf;
        }
    }
}
