package org.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * description： 权限工具辅助类
 * <p/>
 * Created by TIAN FENG on 2017/8/5 19:28
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class PermissionHelper {

    /**
     * 获取没有授予的权限
     */
    public static List<String> getDeniedPermissions(Object object, String[] permissions) {

         List<String> deniedPermissions = new ArrayList<>();
         for (String requestPermission:permissions){
             // 把没有授予过的权限加入到集合
             if(ContextCompat.checkSelfPermission(getActivity(object),requestPermission)
                     == PackageManager.PERMISSION_DENIED){
                 deniedPermissions.add(requestPermission);
             }
         }
         return deniedPermissions;
    }

    // 获取activity
     static Activity getActivity(Object object) {
        return object instanceof Activity? (Activity) object :object instanceof Fragment?((Fragment)object).getActivity():null;
    }

}
