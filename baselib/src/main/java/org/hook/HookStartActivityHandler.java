package org.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.log.L;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/17
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

class HookStartActivityHandler implements InvocationHandler {

    private static final String HOOK_START_ACTIVITY_KEY = "HOOK_START_ACTIVITY_KEY";

    private Object mIActivityManager;
    private Class<? extends Activity> mProxyClazz;
    private Context mContext;

    public HookStartActivityHandler(Object iActivityManager, Class<? extends Activity> clazz, Context context) {
        mIActivityManager = iActivityManager;
        this.mProxyClazz = clazz;
        this.mContext = context.getApplicationContext();
    }

    public Object proxy() {
        return Proxy.newProxyInstance(mIActivityManager.getClass().getClassLoader(), mIActivityManager.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果函数为startActivity
        if (method.getName().equals("startActivity")) {
            // 创建意图为已经注册的意图
            Intent intent = new Intent(mContext, mProxyClazz);
            // 将原始意图包装起来
            intent.putExtra(HOOK_START_ACTIVITY_KEY, (Intent) args[2]);
            // 检测意图为已经注册了的意图
            args[2] = intent;
            L.e("代理class为：" + mProxyClazz.getName());
        }
        return method.invoke(mIActivityManager, args);
    }


    public void hookLaunchActivity() {
        try {
            // 获取ActivityThread的独享
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            // 获取sCurrentActivityThread成员
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");

            sCurrentActivityThreadField.setAccessible(true);
            // 获取sCurrentActivityThread成员的值
            Object sCurrentActivityThreadObj = sCurrentActivityThreadField.get(null);
            // 获取 sCurrentActivityThread下mH的值
            Field mHField = activityThreadClass.getDeclaredField("mH");

            mHField.setAccessible(true);
            // 拿到mH的值
            Handler mHObject = (Handler) mHField.get(sCurrentActivityThreadObj);

            Class mCallbackClass = Handler.class;

            Field mCallbackField = mCallbackClass.getDeclaredField("mCallback");

            mCallbackField.setAccessible(true);
            // 给mH设置callback 在handler中dispatchMessage会先走callback.handleMessage,如果callback.handleMessage返回false在走自己的handleMessage函数
            // 在这里将已经替换的意图还原
            mCallbackField.set(mHObject, new HookHandlerCallBack());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static class HookHandlerCallBack implements Handler.Callback {

        private static final int LAUNCH_ACTIVITY = 100;// 此值为Activitythread中H类的值

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LAUNCH_ACTIVITY:
                    handleLaunchActivity(msg);
                    break;
            }
            return false;
        }

        private void handleLaunchActivity(Message msg) {
            Object activityClientRecordObject = msg.obj;
            try {
                // 获取ActivityClientRecord的成员变脸intent
                Field activityClientRecordObjectField = activityClientRecordObject.getClass().getDeclaredField("intent");

                activityClientRecordObjectField.setAccessible(true);

                Intent activityClientRecordObjectIntent = (Intent) activityClientRecordObjectField.get(activityClientRecordObject);
                // 拿到真实的intent
                Intent originIntent = activityClientRecordObjectIntent.getParcelableExtra(HOOK_START_ACTIVITY_KEY);

                if (originIntent != null) {
                    activityClientRecordObjectField.set(activityClientRecordObject, originIntent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
