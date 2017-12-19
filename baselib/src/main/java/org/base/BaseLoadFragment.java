package org.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * description：懒加载
 * <p/>
 * Created by TIAN FENG on 2017/11/3
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class BaseLoadFragment extends Fragment {

    protected View rootView;
    protected Activity activity;
    protected Context context;
    // 是否显示当前页面
    private boolean mIsUIVisible;
    // 是否已经创建了view
    private boolean mIsViewCreat;

    private int mLayoutId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置根布局
        setContentView();
        // 创建根布局
        creatView(inflater, container);
        // 绑定依赖注入框架
        bindFrameMode();
        // 初始化控件
        initView();
        return rootView;
    }

    private void creatView(LayoutInflater inflater, @Nullable ViewGroup container) {
        if (mLayoutId == 0 && rootView == null) {
            throw new NullPointerException("please call setContentView().");
        }
        if (rootView == null) {
            rootView = inflater.inflate(mLayoutId, container, false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsUIVisible = isVisibleToUser;
        if (mIsUIVisible) {
            loadData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsViewCreat = true;
        loadData();
    }


    private void loadData() {
        if (mIsUIVisible && mIsViewCreat) {
            initData();
            mIsUIVisible = false;
            mIsViewCreat = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsUIVisible = false;
        mIsViewCreat = false;
    }

    public <T extends View> T findViewById(int id) {
        return (T) rootView.findViewById(id);
    }

    public void setContentView(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
    }

    public void setContentView(View rootView) {
        this.rootView = rootView;
    }

    // 布局
    protected abstract void setContentView();

    /**
     * 绑定依赖注入的框架
     */
    protected abstract void bindFrameMode();

    // 控件
    protected abstract void initView();

    // 数据绑定
    protected abstract void initData();
}
