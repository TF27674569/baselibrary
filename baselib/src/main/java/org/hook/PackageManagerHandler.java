package org.hook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/20
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

class PackageManagerHandler implements InvocationHandler {

    private Object mIPackageManager;
    private String mProxyClassName;

    public PackageManagerHandler(Object iPackageManager, String proxyClassName) {
        this.mIPackageManager = iPackageManager;
        mProxyClassName = proxyClassName;
    }

    public Object proxy() throws Exception {
        Class<?> iPackageManagerIntercept = Class.forName("android.content.pm.IPackageManager");
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{iPackageManagerIntercept}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果函数名称为getActivityInfo，在AppCompatActivity下会报找不到类名
        if (method.getName().equals("getActivityInfo")) {
            // 拿到args[0]下的mClass对象并将已经注册的名称赋给rgs[0]
            Class aClass = args[0].getClass();

            Field mClassField = aClass.getDeclaredField("mClass");

            mClassField.setAccessible(true);

            mClassField.set(args[0], mProxyClassName);

        }
        return method.invoke(mIPackageManager, args);
    }
}
