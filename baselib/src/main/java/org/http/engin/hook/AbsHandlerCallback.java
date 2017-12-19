package org.http.engin.hook;

import org.http.engin.OkHttpCall;
import org.http.engin.call.Enqueue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import okhttp3.Callback;

/**
 * description：代理类
 * <p/>
 * Created by TIAN FENG on 2017/11/22
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public abstract class AbsHandlerCallback implements InvocationHandler{

    private Enqueue mEnqueue;

    public Enqueue proxy(Enqueue enqueue){
        mEnqueue = enqueue;
        return (Enqueue) Proxy.newProxyInstance(mEnqueue.getClass().getClassLoader(),mEnqueue.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args[1] = getProxyCallBack((OkHttpCall) args[1]);
        return method.invoke(mEnqueue,args);
    }

    public abstract Callback getProxyCallBack(OkHttpCall call);
}
