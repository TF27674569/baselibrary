package com.demo.fragment;

import android.widget.Button;

import com.demo.R;
import com.demo.util.ToastUtils;

import org.annotation.Event;
import org.base.BaseFragment;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/27
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class MessageFragment extends BaseFragment {

    @Override
    protected void setContentView() {
        setContentView(R.layout.fragment_new);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Event(R.id.btnShop)
    public void btnShopClick(Button btn) {
        ToastUtils.show(btn.getText().toString());
    }

    @Event(R.id.btnComment)
    public void btnCommentClick(Button btn) {
        ToastUtils.show(btn.getText().toString());
    }

    @Event(R.id.btnMoveAnima)
    public void btnMoveAnimaClick(Button btn) {
        ToastUtils.show(btn.getText().toString());
    }

    @Event(R.id.btnRecycler)
    public void btnRecyclerClick(Button btn) {
        ToastUtils.show(btn.getText().toString());
    }

    @Event(R.id.btnLove)
    public void btnLoveClick(Button btn) {
        ToastUtils.show(btn.getText().toString());
    }
}
