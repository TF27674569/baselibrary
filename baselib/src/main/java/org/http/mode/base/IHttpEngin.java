package org.http.mode.base;

import android.content.Context;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpParams;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface IHttpEngin {

    void cancle(Object tag);

    void addDecortor(IHttpEngin decortor);

    void execute(Context context, HttpParams params, BaseCallback callBack);
}
