package org.http.engin;

import android.content.Context;

import org.http.engin.buffer.ParamsTag;
import org.http.engin.call.Enqueue;
import org.http.mode.base.IHttpEngin;
import org.http.mode.callback.BaseCallback;
import org.http.mode.params.ContentTypeParams;
import org.http.mode.params.HttpConfig;
import org.http.mode.params.HttpParams;
import org.http.mode.params.TimeOuts;
import org.log.L;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * description： okHttp执行网络请求引擎
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class OkHttpEngin implements IHttpEngin, Enqueue {

    private OkHttpClient mOkHttpClient;
    private Enqueue mEnqueue;
    private static OkHttpClient sOkHttpClient;

    public OkHttpEngin() {
        this(null);
    }

    public OkHttpEngin(OkHttpClient okHttpClient) {
        sOkHttpClient = okHttpClient;
        mEnqueue = this;
    }

    @Override
    public synchronized void cancle(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        // 获取队列中的任务
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        // 获取执行中的任务
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    @Override
    public void addDecortor(IHttpEngin decortor) {
        throw new IllegalStateException("OkHttpEngin not invoke addDecortor.");
    }

    @Override
    public void execute(Context context, HttpParams params, BaseCallback callBack) {
        initOkhttpClient(params);
        switch (params.getMethod()) {
            case HttpConfig.GET:
                get(context, params, callBack);
                break;
            case HttpConfig.POST:
                post(context, params, callBack);
                break;
            case HttpConfig.UP_LOAD:
                upLoad(context, params, callBack);
                break;
            case HttpConfig.DOWN_LOAD:
                downLoad(context, params, callBack);
                break;
        }
    }

    // 初始话请求工具
    private void initOkhttpClient(HttpParams params) {

        // 如果更改设置了超时和重连则此次请求需要重新创建 sOkHttpClient
        if (sOkHttpClient != null
                && params.getTimeOuts().connectTimeOut != TimeOuts.TIME_OUTS
                && params.getTimeOuts().readTimeOut != TimeOuts.TIME_OUTS
                && params.getTimeOuts().writeTimeOut != TimeOuts.TIME_OUTS
                && !params.isRetry()) {
            mOkHttpClient = sOkHttpClient;
            return;
        }

        // 表示初始化时赋值了 OkHttpClient 请求时改了参数，我们只修改修改后的参数
        if (sOkHttpClient != null) {
            try {
                Class<?> clazz = OkHttpClient.Builder.class;
                Constructor<?> constructor = clazz.getDeclaredConstructor(OkHttpClient.class);
                OkHttpClient.Builder builder = (OkHttpClient.Builder) constructor.newInstance(sOkHttpClient);
                mOkHttpClient = builder
                        .connectTimeout(params.getTimeOuts().connectTimeOut, TimeUnit.SECONDS)
                        .readTimeout(params.getTimeOuts().readTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(params.getTimeOuts().writeTimeOut, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(params.isRetry())
                        .build();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 表示初始化时没有赋值OkHttpClient 请求时改了参数，我们只修改修改后的参数
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(params.getTimeOuts().connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(params.getTimeOuts().readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(params.getTimeOuts().writeTimeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(params.isRetry())
                .build();

    }

    private void get(Context context, HttpParams params, BaseCallback callBack) {
        String url = OkHttpUtils.jointParams(params);

        log(url, params);

        Request.Builder builder = new Request.Builder();

        builder.url(url).get();

        OkHttpUtils.addHeader(builder, params);

        // 方便后期代理
        mEnqueue.enqueue(mOkHttpClient.newCall(builder.build()), new OkHttpCall(params, callBack));
    }

    private void post(Context context, HttpParams params, BaseCallback callBack) {
        log(params.getUrl(), params);

        Request.Builder builder = new Request.Builder();

        builder.url(params.getUrl());

        OkHttpUtils.addHeader(builder, params);

        OkHttpUtils.addBody(builder, params, callBack);

        // 方便后期代理
        mEnqueue.enqueue(mOkHttpClient.newCall(builder.build()), new OkHttpCall(params, callBack));
    }

    private void upLoad(Context context, HttpParams params, BaseCallback callBack) {
        post(context, params, callBack);
    }

    private void downLoad(Context context, HttpParams params, BaseCallback callBack) {
        String url = OkHttpUtils.jointParams(params);

        log(url, params);

        Request.Builder builder = new Request.Builder();

        builder.url(url).get();

        // 计算已经下载了的大小
        long fileSize = 0;
        File file = null;
        try {
            String savePath = OkHttpUtils.isExistDir(params.getPath().substring(0, params.getPath().lastIndexOf("/")));
            // 创建文件
            file = new File(savePath, params.getPath().substring(params.getPath().lastIndexOf("/") + 1));
            // 文件的大小
            fileSize = file.length();
        } catch (IOException e) {
            e.printStackTrace();
            OkHttpUtils.callError(callBack, url, e);
            return;
        }

        OkHttpUtils.addHeader(builder, params, fileSize);

        // 方便后期代理
        mEnqueue.enqueue(mOkHttpClient.newCall(builder.build()), new OkHttpCall(params, callBack, file));
    }

    // 打印Log信息
    private void log(String url, HttpParams params) {
        if (L.isDebug()) {
            String method = null;
            String contentType = null;
            switch (params.getMethod()) {
                case HttpConfig.DOWN_LOAD:
                    method = ParamsTag.DOWN_LOAD;
                    break;
                case HttpConfig.POST:
                    method = ParamsTag.POST;
                    contentType = params.getContentType();
                    break;
                case HttpConfig.GET:
                    method = ParamsTag.GET;
                    break;
                case HttpConfig.UP_LOAD:
                    method = ParamsTag.UP_LOAD;
                    contentType = params.getContentType();
                    break;
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(ParamsTag.HINT)
                    .append(ParamsTag.URL)
                    .append(url)
                    .append(ParamsTag.METHOD)
                    .append(method)
                    .append(ParamsTag.CONTENT_TYPE)
                    .append(contentType)
                    .append(ParamsTag.DATA)
                    .append(params.getData());
            // 打文件路径
            if (params.getMethod() == HttpConfig.DOWN_LOAD) {
                buffer.append(ParamsTag.PATH)
                        .append(params.getPath());
            }
            buffer.append(ParamsTag.FIVE_SPACE)
                    .append(ParamsTag.HEADER);// header标签
            for (Map.Entry<String, String> entry : params.getHeader().entrySet()) {
                buffer.append(entry.getKey())
                        .append(ParamsTag.EQUAL)
                        .append(entry.getValue())
                        .append(ParamsTag.SIX_SPACE);
            }
            buffer.append(ParamsTag.PARAMS);// params标签
            for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
                buffer.append(entry.getKey())
                        .append(ParamsTag.EQUAL)
                        .append(entry.getValue())
                        .append(ParamsTag.SIX_SPACE);
            }
            buffer.append(ParamsTag.FIVE_SPACE)
                    .append(ParamsTag.OTHER);
            for (ContentTypeParams contentTypeParams : params.getTypeParamses()) {
                buffer.append(contentTypeParams);
            }
            if (params.getTypeParamses().size() <= 0) {
                buffer.append(ParamsTag.NULL);
            }
            L.e(buffer.toString());
        }
    }

    @Override
    public void enqueue(Call call, Callback callback) {
        call.enqueue(callback);
    }
}
