package com.demo.fragment;

import android.widget.Button;

import com.demo.R;
import com.demo.activity.BezierShopActivity;
import com.demo.activity.LoveActivity;
import com.demo.activity.MoveItemActivity;
import com.demo.activity.RecyclerViewActivity;
import com.demo.activity.RxJavaActivity;
import com.demo.activity.SmileActivity;

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

public class NewFragment extends BaseFragment {


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
    public void btnShopClick() {
        startActivity(BezierShopActivity.class);
    }

    @Event(R.id.btnComment)
    public void btnCommentClick() {
        startActivity(SmileActivity.class);
    }

    @Event(R.id.btnMoveAnima)
    public void btnMoveAnimaClick() {
        startActivity(MoveItemActivity.class);
    }

    @Event(R.id.btnRecycler)
    public void btnRecyclerClick(Button btnRecycler) {
        startActivity(RecyclerViewActivity.class);
    }

    @Event(R.id.btnLove)
    public void btnLoveClick(Button btnLove) {
        startActivity(LoveActivity.class);
    }

    @Event(R.id.btnRx)
    public void btnRxClick(Button btnRx) {
        startActivity(RxJavaActivity.class);
    }
}
