package com.demo.dispatch;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpParams;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/8.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class AbsInterceptor implements Interceptor {

    private Interceptor mInterceptor;

    @Override
    public void addInterceptor(Interceptor interceptor) {
        mInterceptor = interceptor;
    }

    @Override
    public boolean intercept(HttpParams params, BaseCallback callback) {
        return mInterceptor.intercept(params, callback);
    }
}
