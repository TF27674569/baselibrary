package com.demo.activity;

import android.view.View;

import com.demo.R;
import com.demo.base.NavigationTitle;
import com.demo.view.SmileView;

import org.annotation.Event;
import org.annotation.ViewById;
import org.base.BaseActivity;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/5.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class SmileActivity extends BaseActivity {

    @ViewById(R.id.smileView)
    public SmileView smileView;
    @ViewById(R.id.smileView1)
    public SmileView smileView1;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_smile);
    }

    @Override
    protected void initView() {
        // title
        new NavigationTitle.Builder(this)
                .setTitle("饿了么控件")
                .setBackClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .immersed()
                .build();
    }

    @Override
    protected void initData() {
        smileView.setIsGood(false);
        smileView.setFaceCount(3);
    }

    @Event(R.id.smileView)
    public void smileViewClick(SmileView smileView) {
        startAnima();
    }

    @Event(R.id.smileView1)
    public void smileView1Click(SmileView smileView1) {
        startAnima();
    }

    private void startAnima() {
        smileView.startAnima();
        smileView1.startAnima();
    }
}
