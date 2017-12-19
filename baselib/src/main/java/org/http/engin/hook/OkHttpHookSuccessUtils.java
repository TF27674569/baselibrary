package org.http.engin.hook;

import org.http.engin.OkHttpEngin;
import org.http.engin.call.Enqueue;

import java.lang.reflect.Field;

/**
 * description：hook工具类动态代理
 * <p/>
 * Created by TIAN FENG on 2017/11/22
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class OkHttpHookSuccessUtils {

    private OkHttpEngin okHttpEngin;

    public OkHttpHookSuccessUtils(OkHttpEngin okHttpEngin){
        this.okHttpEngin = okHttpEngin;
    }

    public void hook(AbsHandlerCallback callback){
        Class clazz = OkHttpEngin.class;
        try {
            Field mEnqueueField = clazz.getDeclaredField("mEnqueue");
            mEnqueueField.setAccessible(true);
            Enqueue mEnqueue = (Enqueue) mEnqueueField.get(okHttpEngin);
            mEnqueue = callback.proxy(mEnqueue);
            mEnqueueField.set(okHttpEngin,mEnqueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
