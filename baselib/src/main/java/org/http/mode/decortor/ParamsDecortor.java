package org.http.mode.decortor;

import android.content.Context;

import org.http.mode.callback.BaseCallback;
import org.http.engin.OkHttpCall;
import org.http.engin.OkHttpEngin;
import org.http.engin.hook.AbsHandlerCallback;
import org.http.engin.hook.OkHttpHookSuccessUtils;
import org.http.mode.base.AbsDecortor;
import org.http.mode.base.IHttpEngin;
import org.http.mode.params.HttpParams;
import org.log.L;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * description：demo
 * <p/>
 * Created by TIAN FENG on 2017/11/24
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class ParamsDecortor extends AbsDecortor {

    private String mSsession = "";

    public ParamsDecortor() {

    }

    public ParamsDecortor(IHttpEngin decortor) {
        super(decortor);
        initHeaderParams(getOkHttpEngin(decortor));
    }

    /**
     * 如果decortor不是 OkHttpEngin 不一样请更改此函数
     */
    private OkHttpEngin getOkHttpEngin(IHttpEngin decortor) {
        return (OkHttpEngin) decortor;
    }

    /**
     * 在返回的请求头中拿ssession
     */
    private void initHeaderParams(OkHttpEngin decortor) {
        // 拿到Okhttp引擎
        OkHttpHookSuccessUtils okHttpHookSuccessUtils = new OkHttpHookSuccessUtils(decortor);
        // 代理Callback
        okHttpHookSuccessUtils.hook(new AbsHandlerCallback() {
            @Override
            public Callback getProxyCallBack(OkHttpCall call) {
                // 返回自己的CallBack
                return new SsessionParamsCallBack(call);
            }
        });
    }

    /**
     * 添加ssession保证长连接
     */
    @Override
    public void execute(Context context, HttpParams params, BaseCallback callBack) {
        params.getHeader().put("Set-Cookie", mSsession == null ? "" : mSsession);
        super.execute(context, params, callBack);
    }


    /**
     * 获取返回Header中的Ssession
     */
    private class SsessionParamsCallBack extends OkHttpCall {
        /**
         * 为代理提供的接口
         *
         * @param okHttpCall
         */
        public SsessionParamsCallBack(OkHttpCall okHttpCall) {
            super(okHttpCall);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            // 拿ssession 保证长链接
            mSsession = response.headers().get("Set-Cookie");

            L.e(mSsession);

            // 拦截需要的参数
            super.onResponse(call, response);
        }
    }
}
