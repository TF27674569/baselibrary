package org.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/4/5
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SkinResource {
    // 外部资源通过这个对象获取
    private Resources mSkinResources;
    // skin包名
    private String mSkinPackageName;


    /**
     * @param context       上下文
     * @param skinPath      skin路径
     */
    public SkinResource(Context context, String skinPath) {

        try {
            // 读取本地一个.skin的资源
            Resources superResources = context.getResources();
            // 创建AssetManager
            AssetManager asset = AssetManager.class.newInstance();
            // 添加本地下载好的皮肤
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            // 反射执行方法
            method.invoke(asset, skinPath);
            // 创建一个Resources对象
            mSkinResources = new Resources(asset,
                    superResources.getDisplayMetrics(),
                    superResources.getConfiguration());
            // 获取 skinPath 的包名
            mSkinPackageName = context.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                    .packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据名称获取Drawable
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mSkinResources.getIdentifier(resName, "drawable", mSkinPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据名称获取ColorStateList
     */
    public ColorStateList getColorleByName(String resName) {
        try {
            int resId = mSkinResources.getIdentifier(resName, "color", mSkinPackageName);
            ColorStateList colorStateList = mSkinResources.getColorStateList(resId);
            return colorStateList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
