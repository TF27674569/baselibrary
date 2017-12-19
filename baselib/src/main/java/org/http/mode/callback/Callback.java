package org.http.mode.callback;

/**
 * description： 统一回调接口
 * <p/>
 * Created by TIAN FENG on 2017/10/25
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class Callback {

    public interface CommonCallback<T>{

        void onSuccess(String url, T result);

        void onError(String url, Throwable e);

        void onFinal(String url);
    }

    public interface ProgressCallback<T> extends CommonCallback<T>{

        void onProgress(long total, long current,float progress);

    }
}
