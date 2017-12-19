package org.skin.utils;

import android.content.Context;

import org.skin.config.SkinConfig;


/**
 * Description :  保存状态工具
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SkinPerUtils {


    private static SkinPerUtils mInstance;
    private Context mContext;

    private SkinPerUtils(Context context){
        this.mContext = context.getApplicationContext();
    }

    public static SkinPerUtils getInstance(Context context){
        if (mInstance == null){
            synchronized (SkinPerUtils.class){
                if (mInstance==null){
                    mInstance = new SkinPerUtils(context);
                }
            }
        }
        return mInstance;
    }


    /**
     *保存皮肤路径
     */
    public void saveSkinPath(String skinPath){
        mContext.getSharedPreferences(SkinConfig.SKIN_SHARED_NAME,Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_PATH_NAME,skinPath).commit();
    }


    /**
     *获取皮肤路径
     */
    public String getSkinPath(){
        return mContext.getSharedPreferences(SkinConfig.SKIN_SHARED_NAME,Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME,"");
    }

    /**
     * 清空内容
     */
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
