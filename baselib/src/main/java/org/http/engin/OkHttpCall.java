package org.http.engin;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpConfig;
import org.http.mode.params.HttpParams;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * description：okHttp的结果回调，注意线程在子线程还是主线程
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class OkHttpCall implements Callback {

    private BaseCallback mCallback;
    private HttpParams mParams;
    private File mFile;
    private long mFileSize;

    /**
     * 为代理提供的接口
     */
    public OkHttpCall(OkHttpCall okHttpCall) {
        this.mCallback = okHttpCall.mCallback;
        this.mParams = okHttpCall.mParams;
        this.mFile = okHttpCall.mFile;
        this.mFileSize = okHttpCall.mFileSize;
    }

    OkHttpCall(HttpParams params, BaseCallback callback) {
        this(params, callback, null);
    }

    OkHttpCall(HttpParams params, BaseCallback callback, File file) {
        this.mCallback = callback;
        this.mParams = params;
        this.mFile = file;
        // 如果是下载，且需要断点续传则计算已经下载的大小
        if (file != null && params.isAutoResume()) {
            mFileSize = file.length();
        }
    }

    @Override
    public void onFailure(Call call, final IOException e) {

        // 子线程
        OkHttpUtils.callError(mCallback, mParams.getUrl(), e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        /**
         * 可能需要其他参数，比如header里面的参数，可以直接改这里的源码
         * 如果不改这里的代码最好用动态代理的方式
         *
         * 代理使用在Application中
         * OkHttpHookSuccessUtils okHttpHookSuccessUtils = new OkHttpHookSuccessUtils(OkHttpEngin);
         * okHttpHookSuccessUtils.hook(AbsHandlerCallback);
         * AbsHandlerCallback 需要实现抽象函数 public abstract Callback getProxyCallBack(OkHttpCall call);
         *
         * 返回的Callback为此callback的 public OkHttpCall(OkHttpCall okHttpCall) 这个构造器然后重写onResponse函数 在super之前调用需要获取的参数
         */

        ResponseBody body = response.body();

        // 是否是下载
        if (mParams.getMethod() == HttpConfig.DOWN_LOAD) {
            onDownLoad(body);
            return;
        }

        // 结果
        String result = body.string();

        //结果回调 子线程
        mCallback.onSuccess(mParams.getUrl(), result);
        mCallback.onFinal(mParams.getUrl());
    }

    // 下载
    private void onDownLoad(ResponseBody body) {
        InputStream is = null;
        // 每次读100k
        byte[] buf = new byte[1024 * 100];
        int len;
        FileOutputStream fos = null;
        try {
            // 获取流
            is = body.byteStream();
            // 获取总大小 = 当前剩余大小 + 文件已经下载的大小
            final long total = body.contentLength() + mFileSize;

            // 文件输出流，isAutoResume可以接着写 实现断点续传
            fos = new FileOutputStream(mFile, mParams.isAutoResume());

            // 起始位置为文件已经下载的大小
            long sum = mFileSize;

            // 循环写
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                // 拉一下
                fos.flush();

                // 进度增加
                sum += len;

                // 回调进度
                long finalSum = sum;

                // 子线程
                mCallback.onProgress(total, finalSum);
            }


            // 子线程
            mCallback.onSuccess(mParams.getUrl(), mFile);
            mCallback.onFinal(mParams.getUrl());

        } catch (final Exception e) {
            // 子线程
            OkHttpUtils.callError(mCallback, mParams.getUrl(), e);
        } finally {
            // 关流
            close(is);
            close(fos);
        }
    }

    // 关流
    private void close(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
