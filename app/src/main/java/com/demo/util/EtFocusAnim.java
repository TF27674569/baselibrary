package com.demo.util;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/19.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class EtFocusAnim {
    private Builder.Params P;

    private EtFocusAnim(Builder.Params params) {
        P = params;
    }

    // 绑定参数
    private void apply() {
        P.editText.post(new EtRunAble(P));
    }

    /**
     * Builder
     */
    public static class Builder {

        // 参数
        private Params P;

        /**
         * 构造器
         *
         * @param editText 目标
         */
        public Builder(EditText editText) {
            P = new Params(editText);
        }

        /**
         * 设置动画时间
         */
        public Builder setAnimaTime(long time) {
            P.time = time;
            return this;
        }

        /**
         * 失去焦点是是否显示动画
         */
        public Builder showUnFocusAnima(boolean isShow) {
            P.isShowUnFocusAnima = isShow;
            return this;
        }

        /**
         * 设置插值器
         */
        public Builder setInterpolator(Interpolator interpolator) {
            P.interpolator = interpolator;
            return this;
        }

        /**
         * 构建
         */
        public EtFocusAnim build() {
            EtFocusAnim anim = new EtFocusAnim(P);
            anim.apply();
            return anim;
        }

        private static class Params {
            // 目标
            EditText editText;
            // 时间
            long time = 300;
            // 失去焦点是否显示
            boolean isShowUnFocusAnima;
            // 默认线性插值器
            Interpolator interpolator = new LinearInterpolator();

            Params(EditText editText) {
                this.editText = editText;
            }
        }
    }

    // 执行体 主要逻辑实现
    private class EtRunAble implements Runnable, View.OnFocusChangeListener {
        // 参数集合
        private Builder.Params mParams;
        // 控件宽和最大宽
        private int mEtWidth, mMaxWidth;

        // 构造器
        private EtRunAble(Builder.Params params) {
            mParams = params;
        }

        @Override
        public void run() {
            // 获取控件宽
            mEtWidth = mParams.editText.getWidth();
            // 获取最大宽
            mMaxWidth = ((ViewGroup) mParams.editText.getParent()).getWidth()
                    - mParams.editText.getPaddingRight() - mParams.editText.getPaddingLeft();

            // 设置焦点监听
            mParams.editText.setOnFocusChangeListener(this);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // 显示动画
            showAnimation(hasFocus);
        }

        private void showAnimation(boolean hasFocus) {
            // 失去焦点时 不执行动画
            if (!hasFocus && !mParams.isShowUnFocusAnima) return;

            // 可以执行动画时创建动画
            ValueAnimator animator = hasFocus ? ValueAnimator.ofObject(new EditTextEvaluator(), mEtWidth, mMaxWidth) :
                    ValueAnimator.ofObject(new EditTextEvaluator(), mMaxWidth, mEtWidth);

            // 动画只需时间
            animator.setDuration(mParams.time);
            // 插值器
            animator.setInterpolator(mParams.interpolator);
            // 监听值
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 获取估值器算出的值
                    Integer value = (Integer) animation.getAnimatedValue();
                    // 获取editText 的 LayoutParams
                    ViewGroup.LayoutParams params = mParams.editText.getLayoutParams();
                    // 动态改变值
                    params.width = value;
                    mParams.editText.setLayoutParams(params);
                }
            });
            // 开启动画
            animator.start();
        }
    }

    // 估值器
    private class EditTextEvaluator implements TypeEvaluator<Integer> {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            return (int) (startValue + fraction * (endValue - startValue));
        }
    }
}
