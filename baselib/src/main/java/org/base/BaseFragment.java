package org.base;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.api.ViewUtils;

/**
 * description：基本的Fragment 实现懒加载
 * <p/>
 * Created by TIAN FENG on 2017/11/6
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class BaseFragment extends BaseLoadFragment {
    @Override
    protected void bindFrameMode() {
        // 编译框架至少还需要两个modle,打成jar比较方便 如果需要重写toast，
        // 重写拦截请在application中ViewUtils.setViewBinder()中重写ViewBinder
        // 相关注解
        // CheckNet: 检测网络,无网络toast "当前无网络~"
        // EchoEnable : 防止重复点击 ViewUtils.setMinEchoTime设置重复点击间隔
        // Event : 点击事件回调函数(只支持点击事件)
        // Extra : intent bundle 传参 Activity fragment可用
        // ViewById : 初始化控件
        ViewUtils.bind(this, rootView);
    }

    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(context, clazz));
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
