package com.demo.activity;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.demo.R;

import org.base.BaseActivity;
import org.utils.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/6
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class LoadingActivity extends BaseActivity {

    private static final long WAIT_TIME = 3000;

    @Override
    protected void setContentView() {
        hideVirtualKey(this);
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void initView() {
        StatusBarUtils.statusBarTranslucent(this);
    }

    @Override
    protected void initData() {
        Observable.timer(WAIT_TIME, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        startActivity(MainActivity.class);
                        finish();
                    }
                });

        // 倒计时按钮显示版本
//        Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
//                .take(WAIT_TIME / 1000 + 1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Function<Long, Integer>() {
//                    @Override
//                    public Integer apply(Long aLong) throws Exception {
//                        /**
//                         * 这里的aLong = 1, 2, 3
//                         */
//                        return (int) (WAIT_TIME / 1000 - aLong);
//                    }
//                })
//                .filter(new Predicate<Integer>() { // 过滤掉无用数据
//                    @Override
//                    public boolean test(Integer integer) throws Exception {
//                        // 可以设置button的倒计时文本
//                        // 如果 点击跳转 请在跳转之前或之后调用disposable.dispose(); 避免泄露和重复启动
//                        return integer == 1;
//                    }
//                })
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer aLong) throws Exception {
//                        startActivity(MainActivity.class);
//                        finish();
//                    }
//                });

    }

    /**
     * 隐藏Android底部的虚拟按键
     */
    public static void hideVirtualKey(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
