package com.demo.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.R;

import org.navigationbar.AbsNavigationBar;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/24
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class NavigationTitle extends AbsNavigationBar<NavigationTitle.Builder.Params> {


    private NavigationTitle(Builder.Params params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.layout_navigation_title;
    }

    public TextView getTitle() {
        return findViewById(R.id.tvTitle);
    }

    public int getTitleHeight() {
        return getRootView().getLayoutParams().height;
    }

    @Override
    public void applyView() {
        setOnClickListener(R.id.titleBack, getParams().backClickListener);
        setText(R.id.tvTitle, getParams().title);
    }


    public static class Builder extends AbsNavigationBar.Builder {

        private Params P;

        public Builder(Context context) {
            this(context, null);
        }

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new Params(context, parent);
        }


        public Builder setTitle(String title) {
            P.title = title;
            return this;
        }

        // 沉浸式
        public Builder immersed() {
            P.mImmersed = true;
            return this;
        }

        public Builder setBackClickListener(View.OnClickListener backClickListener) {
            P.backClickListener = backClickListener;
            return this;
        }

        @Override
        public AbsNavigationBar build() {
            return new NavigationTitle(P);
        }

        static class Params extends AbsNavigationBar.Builder.AbsNavigationParams {
            String title;
            View.OnClickListener backClickListener;

            Params(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
