package org.http.mode.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpParams;

/**
 * description：基本的装饰 注意线程
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class AbsDecortor implements IHttpEngin {

    public IHttpEngin decortor;
    private Handler mHandler;

    public AbsDecortor() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public AbsDecortor(IHttpEngin decortor) {
        this();
        addDecortor(decortor);
    }

    @Override
    public void cancle(Object tag) {
        decortor.cancle(tag);
    }

    @Override
    public void addDecortor(IHttpEngin decortor) {
        if (decortor == null) {
            throw new NullPointerException("decortor is null!");
        }
        this.decortor = decortor;
    }

    /**
     * 复写此函数进行功能拓展
     *
     * @param context  上下文
     * @param params   请求参数封装
     * @param callBack 回调
     */
    @Override
    public void execute(Context context, HttpParams params, BaseCallback callBack) {
        decortor.execute(context, params, callBack);
    }

    /**
     * 回调主线程
     *
     * @param runnable
     */
    protected void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

}
