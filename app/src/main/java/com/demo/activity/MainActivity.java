package com.demo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.demo.R;
import com.demo.base.NavigationTitle;
import com.demo.fragment.FindFragment;
import com.demo.fragment.HomeFragment;
import com.demo.fragment.MessageFragment;
import com.demo.fragment.NewFragment;

import org.adapter.BaseFragmentAdapter;
import org.annotation.CheckNet;
import org.annotation.EchoEnable;
import org.annotation.Event;
import org.annotation.ViewById;
import org.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {


    @ViewById(R.id.vpFram)
    public ViewPager vpFram;
    //首页
    @ViewById(R.id.rbHome)
    public RadioButton rbHome;
    //发现
    @ViewById(R.id.rbFind)
    public RadioButton rbFind;
    //新鲜
    @ViewById(R.id.rbNew)
    public RadioButton rbNew;
    //消息
    @ViewById(R.id.rbMessage)
    public RadioButton rbMessage;
    @ViewById(R.id.radioGroup)
    public RadioGroup radioGroup;

    private List<Fragment> mFragments;

    private TextView mTitleTv;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        // 整个title 可以不用再布局中引入 include 这里通过DecorView添加
        // 注意这里的 title 在状态栏Statebar的上面处理沉浸式是直接这个函数immersed()
        // NavigationTitle的功能根据需求增加
        NavigationTitle title = (NavigationTitle) new NavigationTitle.Builder(this)
                .setTitle("首页")
                .immersed()
                .build();
        mTitleTv = title.getTitle();
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new FindFragment());
        mFragments.add(new NewFragment());
        mFragments.add(new MessageFragment());
        vpFram.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(), mFragments));
        vpFram.setOffscreenPageLimit(4);

        initListener();
    }

    private void initListener() {
        vpFram.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rbHome);
                        mTitleTv.setText("首页");
                        break;
                    case 1:
                        radioGroup.check(R.id.rbFind);
                        mTitleTv.setText("发现");
                        break;
                    case 2:
                        radioGroup.check(R.id.rbNew);
                        mTitleTv.setText("新鲜");
                        break;
                    case 3:
                        radioGroup.check(R.id.rbMessage);
                        mTitleTv.setText("消息");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 屏幕快速启动
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }

    @EchoEnable// 防止重复点击标签
    @CheckNet // 检测网络标签
    @Event(R.id.rbHome)
    public void rbHomeClick(RadioButton rbHome) {
        vpFram.setCurrentItem(0);
    }

    @EchoEnable// 防止重复点击标签
    @CheckNet // 检测网络标签
    @Event(R.id.rbFind)
    public void rbFindClick(RadioButton rbFind) {
        vpFram.setCurrentItem(1);
    }

    @EchoEnable// 防止重复点击标签
    @CheckNet // 检测网络标签
    @Event(R.id.rbNew)
    public void rbNewClick(RadioButton rbNew) {
        vpFram.setCurrentItem(2);
    }

    @EchoEnable// 防止重复点击标签
    @CheckNet // 检测网络标签
    @Event(R.id.rbMessage)
    public void rbMessageClick(RadioButton rbMessage) {
        vpFram.setCurrentItem(3);
    }
}
