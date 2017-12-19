package org.ui.util;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class ActivityUtils {
    private static volatile ActivityUtils mInstance;
    private Map<Class,Activity> mActivityMap ;

    private ActivityUtils(){
        mActivityMap = new HashMap<>();
    }

    public static ActivityUtils get(){
        if (mInstance == null){
            synchronized (ActivityUtils.class){
                if (mInstance == null){
                    mInstance = new ActivityUtils();
                }
            }
        }
        return mInstance;
    }
    public void add(Activity activity){
        mActivityMap.put(activity.getClass(),activity);
    }

    public Activity query(Class<? extends Activity> clazz){
         return  mActivityMap.get(clazz);
    }

    public void remove(Class<? extends Activity> clazz){
        mActivityMap.remove(clazz);
    }

    public void finish(Class<? extends Activity> clazz){
        Activity activity = mActivityMap.get(clazz);
        if (activity !=null){
            activity.finish();
        }
        mActivityMap.remove(clazz);
    }

    public void finish(Activity activity){
        if (activity !=null){
            activity.finish();
        }
        mActivityMap.remove(activity.getClass());
    }

}
