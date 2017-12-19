package org.http;

import android.content.Context;

import org.http.mode.base.AbsDecortor;
import org.http.mode.base.IHttpEngin;
import org.http.mode.callback.Callback;
import org.http.mode.callback.HttpCallback;
import org.http.mode.params.HttpConfig;
import org.http.mode.params.HttpParams;


/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class HttpUtils {
    // 桥
    private static IHttpEngin sEngin;
    private IHttpEngin mCallEngin;
    private Context mContext;
    private HttpParams mParams;

    // 请求基本引擎
    public static void init(IHttpEngin engin) {
        sEngin = engin;
    }

    public static IHttpEngin getBaseEngin() {
        return sEngin;
    }

    // 私有构造器
    private HttpUtils(Context context) {
        mContext = context;
        mCallEngin = sEngin;
        mParams = new HttpParams();
    }

    // 入口
    public static HttpUtils with(Context context) {
        if (sEngin == null) {
            throw new NullPointerException("IHttpEngin is null , please call method init by Application.");
        }
        return new HttpUtils(context);
    }

    // 地址
    public HttpUtils url(String url) {
        mParams.setUrl(url);
        return this;
    }

    // 标记
    public HttpUtils tag(Object tag) {
        mParams.setTag(tag);
        return this;
    }

    // post提交类型，默认form-data
    public HttpUtils type(String contentType) {
        mParams.contentType(contentType);
        return this;
    }

    // 链接超时 单位秒 默认10s
    public HttpUtils connectTimeout(int connectTimeout) {
        mParams.setConnectTimeOut(connectTimeout);
        return this;
    }

    // 读取超时 单位秒 默认10s
    public HttpUtils readTimeout(int readTimeout) {
        mParams.setReadTimeOut(readTimeout);
        return this;
    }

    // 写入超时 单位秒 默认10s
    public HttpUtils writeTimeout(int writeTimeout) {
        mParams.setWriteTimeOut(writeTimeout);
        return this;
    }

    // 添加装饰器
    public HttpUtils addDecortor(AbsDecortor decortor) {
        decortor.addDecortor(mCallEngin);
        mCallEngin = decortor;
        return this;
    }

    // 请求头
    public HttpUtils addHeader(String key, String value) {
        mParams.addHeader(key, value);
        return this;
    }

    // 请求参数
    public HttpUtils addParams(String key, String value) {
        mParams.addParams(key, value);
        return this;
    }

    // 请求参数，上传文件只能以multipart/form-data方式提交
    public HttpUtils addParams(String key, Object value) {
        mParams.addParams(key, value);
        return this;
    }

    //一般上传文件使用，上传文件只能以multipart/form-data方式提交
    public HttpUtils addParams(String key, Object value, String contentType) {
        mParams.addParams(key, value, contentType);
        return this;
    }

    // 断点续传
    public HttpUtils autoResume() {
        mParams.setAutoResume(true);
        return this;
    }

    // 下载路径
    public HttpUtils path(String pathOrName) {
        mParams.setPath(pathOrName);
        return this;
    }

    // 其他提交
    public HttpUtils otherSubmit(String contentType, String data) {
        mParams.otherSubmit(contentType, data);
        return this;
    }

    // 失败重试
    public HttpUtils retryOnConnectionFailure(boolean isRetry) {
        mParams.setRetryOnConnectionFailure(isRetry);
        return this;
    }

    // get
    public void get() {
        get(null);
    }

    // post
    public void post() {
        post(null);
    }

    // get
    public <T> void get(Callback.CommonCallback<T> callback) {
        mParams.setMethod(HttpConfig.GET);
        execute(callback);
    }

    // post
    public <T> void post(Callback.CommonCallback<T> callback) {
        mParams.setMethod(HttpConfig.POST);
        execute(callback);
    }

    // upLoad
    public <T> void upLoad(Callback.ProgressCallback<T> callback) {
        mParams.setMethod(HttpConfig.UP_LOAD);
        execute(callback);
    }

    // downLoad
    public <T> void downLoad(Callback.ProgressCallback<T> callback) {
        mParams.setMethod(HttpConfig.DOWN_LOAD);
        execute(callback);
    }

    // 执行
    private <T> void execute(Callback.CommonCallback<T> callback) {
        mCallEngin.execute(mContext, mParams, new HttpCallback<T>(callback));
    }

    // 取消
    public static void cancle(Object tag) {
        sEngin.cancle(tag);
    }
}
