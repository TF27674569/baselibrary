package org.ui;

import android.app.Activity;
import android.content.Intent;

import org.ui.activity.CameraActivity;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class TackCamera {

    private Activity mActivity;
    private int mRequestCode;

    private TackCamera(Activity activity) {
        this.mActivity = activity;
    }

    public static TackCamera with(Activity activity) {
        return new TackCamera(activity);
    }

    public TackCamera requestCode(int code) {
        this.mRequestCode = code;
        return this;
    }

    public void start(){
        Intent intent = new Intent(mActivity, CameraActivity.class);
        mActivity.startActivityForResult(intent,mRequestCode);
    }
}
