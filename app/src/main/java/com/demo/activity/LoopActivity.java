package com.demo.activity;

import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.demo.R;
import com.demo.util.EtFocusAnim;

import org.annotation.Event;
import org.annotation.ViewById;
import org.base.BaseActivity;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/15.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class LoopActivity extends BaseActivity {


    @ViewById(R.id.edittext)
    public EditText edittext;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_loop);
    }

    @Override
    protected void initView() {
        new EtFocusAnim.Builder(edittext)
                .setAnimaTime(500)
                .setInterpolator(new DecelerateInterpolator())
                .showUnFocusAnima(true)
                .build();
    }

    @Override
    protected void initData() {

    }

    @Event(R.id.click1)
    public void click1Click(Button click1)
    {
        edittext.clearFocus();
    }

    @Event(R.id.click2)
    public void click2Click(Button click2) {

    }

    @Event(R.id.click3)
    public void click3Click(Button click3) {

    }
}
