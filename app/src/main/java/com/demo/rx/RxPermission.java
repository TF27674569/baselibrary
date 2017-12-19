package com.demo.rx;

import android.app.Activity;
import android.os.Build;

import org.permission.Custom;
import org.permission.PermissionHelper;
import org.permission.PermissionUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/14.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class RxPermission {

    private Activity mActivity;

    private RxPermission(Activity activity) {
        mActivity = activity;
    }

    public static RxPermission create(Activity activity) {
        return new RxPermission(activity);
    }

    public Observable<Boolean> request(final String... permissions) {

        // 检测版本 < 棉花糖（6.0）
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return Observable.just(true);
        }
        // 需要申请的权限中 获取没有授予过得权限
        List<String> deniedPermissions = PermissionHelper.getDeniedPermissions(mActivity, permissions);
        // 是否全部授予
        if (deniedPermissions.size() == 0) {
            // 全部都是授予过的
            return Observable.just(true);
        }

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                PermissionUtils.with(mActivity).permissions(permissions)
                        .request(new Custom() {
                            @Override
                            public void onNext(boolean isSuccess) {
                                emitter.onNext(isSuccess);
                                emitter.onComplete();
                            }
                        });
            }
        });
    }
}
