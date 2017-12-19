package org.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.utils.StatusBarUtils;

/**
 * description：title导航栏，通过DecorView来加载title控件，不用在布局中incloud布局
 * <p/>
 * Created by TIAN FENG on 2017/11/8
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;

    private View mNavigationView;

    protected AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }


    protected View getRootView() {
        return mNavigationView;
    }

    public P getParams() {
        return mParams;
    }


    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }


    /**
     * 设置是否显示
     */
    protected void setVisibility(int viewId, int visibility) {
        findViewById(viewId).setVisibility(visibility);
    }


    /**
     * 设置点击
     *
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        if (listener != null)
            view.setVisibility(View.VISIBLE);
        view.setOnClickListener(listener);
    }


    public <T extends View> T findViewById(int viewId) {
        return (T) mNavigationView.findViewById(viewId);
    }

    /**
     * 绑定和创建View
     */
    private void createAndBindView() {
        // 如果构造器中没有传入 mParent，我们就拿DecorView的第一个子view
        if (mParams.mParent == null) {
            // 获取activity的根布局
            ViewGroup activityRoot = (ViewGroup) ((Activity) (mParams.mContext))
                    .getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }

        // 一般不会再为空
        if (mParams.mParent == null) {
            return;
        }

        // 注册传过来的布局
        mNavigationView = LayoutInflater.from(mParams.mContext).
                inflate(bindLayoutId(), mParams.mParent, false);

        // 将布局加入到我们的头部
        mParams.mParent.addView(mNavigationView, 0);

        applyView();
        immersed();
    }

    // 沉浸式
    private void immersed() {
        // 如果需要沉浸式
        if (getParams().mImmersed) {
            // 获取rootView的参数
            ViewGroup.LayoutParams params = mNavigationView.getLayoutParams();
            // 状态栏高
            int statHeight = StatusBarUtils.getStatusBarHeight(getParams().mContext);
            // 修改rootView高
            params.height = params.height + statHeight;
            // 重新设置rootView的参数
            mNavigationView.setLayoutParams(params);
            // 隐藏状态栏
            StatusBarUtils.statusBarTranslucent((Activity) getParams().mContext);

            if (mNavigationView instanceof ViewGroup) {
                // 获取跟布局
                ViewGroup rootView = (ViewGroup) mNavigationView;
                // 重新布局子view
                for (int i = 0; i < rootView.getChildCount(); i++) {
                    View childView = rootView.getChildAt(i);
                    childView.setPadding(childView.getPaddingLeft(),
                            statHeight + childView.getPaddingTop(),
                            childView.getPaddingRight(),
                            childView.getPaddingBottom());
                }
            } else {
                mNavigationView.setPadding(0, statHeight, 0, 0);
            }
        }
    }


    public abstract static class Builder {

        // 构造器
        public Builder(Context context, ViewGroup parent) {

        }

        public Builder(Context context) {
            this(context, null);
        }

        public abstract <T extends AbsNavigationBar> T build();


        public static class AbsNavigationParams {
            public Context mContext;
            public ViewGroup mParent;
            public boolean mImmersed = false;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
