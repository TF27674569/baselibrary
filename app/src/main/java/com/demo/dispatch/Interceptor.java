package com.demo.dispatch;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpParams;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface Interceptor {


    void addInterceptor(Interceptor interceptor);

    /**
     * 拦截器
     *
     * @param params  参数
     * @param callback callback
     * @return 是否继续进入下一个拦截器 true 不进入 false 进入
     */
    boolean intercept(HttpParams params, BaseCallback callback);

}
