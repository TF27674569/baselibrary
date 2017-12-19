package org.http.mode.base;


import org.http.mode.callback.BaseCallback;

import java.io.File;

/**
 * description：基本装饰回调
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BaseCallbackAdapter implements BaseCallback {

    protected BaseCallback callback;

    public BaseCallbackAdapter(BaseCallback callback){
        this.callback = callback;
    }

    @Override
    public void onProgress(long total, long current) {
        callback.onProgress(total, current);
    }

    @Override
    public void onSuccess(String url, File file) {
        callback.onSuccess(url, file);
    }

    @Override
    public void onSuccess(String url, String result) {
        callback.onSuccess(url, result);
    }

    @Override
    public void onError(String url, Throwable e) {
        callback.onError(url, e);
    }

    @Override
    public void onFinal(String url) {
        callback.onFinal(url);
    }
}
