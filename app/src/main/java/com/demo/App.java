package com.demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.util.ToastUtils;

import org.adapter.recycler.EAdapter;
import org.adapter.recycler.mode.EImageLoader;
import org.http.HttpUtils;
import org.http.engin.OkHttpEngin;
import org.http.mode.decortor.ParamsDecortor;
import org.log.L;
import org.skin.SkinManager;
import org.utils.PreferencesUtil;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/6
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // 插件换肤
        SkinManager.getInstance().init(this);
        // 日志
        L.debug(true);
        MultiDex.install(this);
//        HookStartActivity.hookStartActivity(this, MainActivity.class);

        // 网络
        HttpUtils.init(new ParamsDecortor(new OkHttpEngin()));

        PreferencesUtil.init(this, "demo");

        EAdapter.setImageLoader(new EImageLoader() {
            @Override
            public void displayImage(Context context, ImageView imageView, String imagePath) {
                Glide.with(context).load(imagePath).into(imageView);
            }
        });

        ToastUtils.init(this);
//        Config.DEBUG = true;
//        UMShareAPI.init(this,"5a30c5058f4a9d408f000116");
    }

}
