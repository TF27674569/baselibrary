package org.ui.view.camera.util;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;


/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class GoogleDetectListenerImpl implements Camera.FaceDetectionListener{
    private Handler mHandler;///用于向主线程发送信息
    private Context mContext;

    public GoogleDetectListenerImpl(Context mContext,Handler mHandler) {
        this.mHandler = mHandler;
        this.mContext = mContext;
    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces!=null){
            Message msg = mHandler.obtainMessage();
            msg.what = CameraUtil.RECEIVE_FACE_MSG;
            msg.obj = faces;
            msg.sendToTarget();
        }

    }
}
