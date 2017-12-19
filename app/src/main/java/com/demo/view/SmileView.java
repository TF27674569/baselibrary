package com.demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.R;

import org.annotation.ViewById;
import org.api.ViewUtils;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/5.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class SmileView extends LinearLayout {


    @ViewById(R.id.smileFace)
    public ImageView smileFace;
    @ViewById(R.id.backGround)
    public LinearLayout backGround;

    // 最大高度
    private int mSmileFaceMaxHeight;
    // 容器初始高度，容器当前高度
    private int mInitHeight, mCurrentHeight;

    private int mMaxValue;

    private boolean mIsGood = true;

    private AnimationListener mAnimationListener;

    public SmileView(Context context) {
        this(context, null);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_smile, this);
        ViewUtils.bind(this);
        init();
    }

    // 初始化
    private void init() {
        mInitHeight = smileFace.getLayoutParams().height + backGround.getPaddingBottom() + backGround.getPaddingTop();
        mSmileFaceMaxHeight = mInitHeight * 5;
        backGround.setBackgroundResource(R.drawable.white_background);
    }
    /*****************************************************************************************************************/

    /**
     * 设置最大高度的个数
     */
    public void setFaceCount(int count) {
        mSmileFaceMaxHeight = mInitHeight * count;
    }

    /**
     * 是否点赞
     */
    public void setIsGood(boolean isGood) {
        mIsGood = isGood;
        smileFace.setBackgroundResource(isGood ? R.drawable.animation_like : R.drawable.dislike_1);
    }

    /**
     * 启动动画
     */
    public void startAnima() {
        onStartAnima();
    }

    /**
     * 监听
     */
    public void setAnimationListener(AnimationListener listener) {
        mAnimationListener = listener;
    }

    /********************************************************************************************************************/
    /**
     * 设置最大值
     */
    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    /**
     * 设置当前值
     */
    public void setCurrentValue(int value) {
        // 当前进度
        float progress = value * 1f / mMaxValue;
        // 当前高度
        mCurrentHeight = progress * mSmileFaceMaxHeight < mInitHeight ? mInitHeight : (int) (progress * mSmileFaceMaxHeight);
        // 设置高度
        setHeight();
    }

    // 设置高
    private void setHeight() {
        // 控件参数
        ViewGroup.LayoutParams params = backGround.getLayoutParams();
        // 修改高度
        params.height = mCurrentHeight;
        // 重设参数
        backGround.setLayoutParams(params);
    }

    /**
     * 起始动画
     */
    private void onStartAnima() {
        // 设置不可用，避免重复点击
        setEnabled(false);
        backGround.setBackgroundResource(R.drawable.yellow_background);
        // 开启伸缩动画
        ValueAnimator animator = ValueAnimator.ofInt(mInitHeight, mSmileFaceMaxHeight);

        animator.setDuration(800)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int value = (int) valueAnimator.getAnimatedValue();
                        mCurrentHeight = value;
                        setHeight();
                    }
                });
        // 插值器
        animator.setInterpolator(new DecelerateInterpolator());
        // 监听动画结束执行帧动画
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startFrameAnima();
            }
        });
        animator.start();
    }

    // 开启帧动画
    private void startFrameAnima() {
        //添加动画资源  获得帧动画
        if (mIsGood) {
            smileFace.setBackgroundResource(R.drawable.animation_like);
        } else {
            smileFace.setBackgroundResource(R.drawable.animation_dislike);
        }
        // drawable动画
        AnimationDrawable animationDrawable = (AnimationDrawable) smileFace.getBackground();
        // 启动
        animationDrawable.start();
        // 图片属性动画
        setImageAnima();
    }

    //图片属性动画
    private void setImageAnima() {
        ObjectAnimator animator = mIsGood ? ObjectAnimator.ofFloat(smileFace, "translationY",/* -10.0f, */0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0) :
                ObjectAnimator.ofFloat(smileFace, "translationX", /*-10.0f,*/ 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(1500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onEndAnima(); //执行收回动画
            }
        });

    }


    private void onEndAnima() {
        // 开启伸缩动画
        final ValueAnimator animator = ValueAnimator.ofInt(mSmileFaceMaxHeight, mInitHeight);

        animator.setDuration(300)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int value = (int) valueAnimator.getAnimatedValue();
                        mCurrentHeight = value;
                        setHeight();
                    }
                });
        // 插值器
        animator.setInterpolator(new DecelerateInterpolator());
        // 监听动画结束执行帧动画
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                smileFace.setBackgroundResource(mIsGood ? R.drawable.like_1 : R.drawable.dislike_1);
                backGround.setBackgroundResource(R.drawable.white_background);
                // 回调结束
                if (mAnimationListener != null) {
                    mAnimationListener.onEnd();
                }
                setEnabled(true);
            }
        });
        animator.start();
    }


    public interface AnimationListener {
        void onEnd();
    }
}
