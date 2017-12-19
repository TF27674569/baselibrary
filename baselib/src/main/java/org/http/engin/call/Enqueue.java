package org.http.engin.call;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * description：为代理服务的类，enqueue 实现为okhttp添加进队列
 * <p/>
 * Created by TIAN FENG on 2017/11/22
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface Enqueue {

    void enqueue(Call call, Callback callback);
}
