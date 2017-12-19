package com.demo.util;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/8.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class ToastUtils {

    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;


    private static Context sContext;


    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    /**
     * 显示Toast
     *
     * @param message
     */
    public static void show(String message) {
        if (toast == null) {
            toast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                toast.setText(message);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}