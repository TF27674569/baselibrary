package org.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;

import java.util.List;

/**
 * description： 动态权限申请
 * <p/>
 * Created by TIAN FENG on 2017/8/5 19:00
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class PermissionUtils implements Custom {

    static Custom sCustom;
    // 操作对象
    private Activity mActivity;
    // 请求码
    private int mRequestCode;
    // 权限
    private String[] mPermissions;
    // 状态回调
    private Custom mCustom;


    private PermissionUtils(Activity activity) {
        mActivity = activity;
        sCustom = this;
    }


    // 从哪里请求
    public static PermissionUtils with(Activity activity) {
        return new PermissionUtils(activity);
    }


    // 权限
    public PermissionUtils permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }


    // 开始请求
    public void request(Custom callback) {
        this.mCustom = callback;
        quest();
    }

    // 实现
    @SuppressLint("ObsoleteSdkInt")
    private void quest() {
        // 检测版本 < 棉花糖（6.0）
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onNext(true);
            return;
        }
        // 需要申请的权限中 获取没有授予过得权限
        List<String> deniedPermissions = PermissionHelper.getDeniedPermissions(mActivity, mPermissions);
        // 是否全部授予
        if (deniedPermissions.size() == 0) {
            // 全部都是授予过的
            onNext(true);
        } else {
            PermissionActivity.start(mActivity, deniedPermissions.toArray(new String[deniedPermissions.size()]));
        }
    }


    /**
     * 释放资源
     */
    private void onDestroy() {
        mActivity = null;
        mRequestCode = 0;
        mPermissions = null;
        mCustom = null;
    }

    @Override
    public void onNext(boolean isSuccess) {
        mCustom.onNext(isSuccess);
        onDestroy();
    }
}
