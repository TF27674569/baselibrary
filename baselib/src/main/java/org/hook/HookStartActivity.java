package org.hook;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * description：免MainFest注册，hook startActivity函数
 * <p/>
 * Created by TIAN FENG on 2017/11/17
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class HookStartActivity {

    /**
     * 1.通过动态代理IActivityManager的startActivity函数，将未注册的Activity在检测时用已经注册的替换
     * 2.在检测完成后将替换的Activity还原，从而绕过MainFest的注册
     * @param context 上下文
     * @param clazz 已经注册的class
     */
    public static void hookStartActivity(Context context,Class<? extends Activity> clazz){
        try {

            // 获取AMS的class
            Class activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            // 获取gDefault静态成员
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            // 可操作私有
            gDefaultField.setAccessible(true);
            // 获取静态对象的值
            Object gDefault = gDefaultField.get(null);
            // 获取Singleton的class
            Class singletonClass = Class.forName("android.util.Singleton");
            // 获取成员变量mInstance
            Field mInstanceField =  singletonClass.getDeclaredField("mInstance");
            // 可操作私有
            mInstanceField.setAccessible(true);
            // 获取IActivityManager接口对象，后续进行代理
            Object iActivityManagerObject = mInstanceField.get(gDefault);
            // 创建代理对象
            HookStartActivityHandler handler = new HookStartActivityHandler(iActivityManagerObject,clazz,context);
            // 开始代理
            Object amsObj = handler.proxy();
            // 替换IActivityManager接口为代理接口
            mInstanceField.set(gDefault,amsObj);

            // 兼容AppCompatActivity，代理IPackageManager
            hookPackageManager(clazz);

            // 代理IPackageManager，需要在启动之前处理
            handler.hookLaunchActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hookPackageManager(Class<? extends Activity> clazz) {
        try {
            // 获取ActivityThread的calss
            Class<?> forName = Class.forName("android.app.ActivityThread");
            // 获取静态sCurrentActivityThread的file
            Field field = forName.getDeclaredField("sCurrentActivityThread");
            // 可操作私有
            field.setAccessible(true);
            // 获取静态sCurrentActivityThread的值
            Object sCurrentActivityThreadObj = field.get(null);
            // 获取函数getPackageManager
            Method getPackageManager = sCurrentActivityThreadObj.getClass().getDeclaredMethod("getPackageManager");
            // 实现函数getPackageManager返回sPackageManager的值
            Object iPackageManager = getPackageManager.invoke(sCurrentActivityThreadObj);
            //代理IPackageManager
            PackageManagerHandler handler = new PackageManagerHandler(iPackageManager,clazz.getName());
            // 开始代理
            Object proxy = handler.proxy();
            // 获取 sPackageManager 属性
            Field iPackageManagerField = sCurrentActivityThreadObj.getClass().getDeclaredField("sPackageManager");
            // 可操作性私有
            iPackageManagerField.setAccessible(true);
            // 设置对象为代理对象
            iPackageManagerField.set(sCurrentActivityThreadObj, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
