package com.demo.dispatch.test;

import com.demo.dispatch.AbsInterceptor;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpParams;
import org.log.L;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/8.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class DialogInterceptor extends AbsInterceptor{

    @Override
    public boolean intercept(HttpParams params, BaseCallback callback) {

        if (super.intercept(params, callback)) {
            return true;
        }
        L.e("创建dialog，一般是不会拦截此请求的");

        return false;
    }
}
