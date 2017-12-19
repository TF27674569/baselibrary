package org.http.mode.callback;

import java.io.File;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface BaseCallback {

    void onProgress(long total , long current);

    void onSuccess(String url ,File file);

    void onSuccess(String url ,String result);

    void onError(String url ,Throwable e);

    void onFinal(String url);
}
