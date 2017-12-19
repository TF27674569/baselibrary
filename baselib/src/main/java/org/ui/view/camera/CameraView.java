package org.ui.view.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.ui.view.camera.util.CameraUtil;


/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Handler mHandler;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        surfaceHolder = getHolder();
//        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
//        surfaceHolder.addCallback(this);
    }

    public void openSurface(){
        surfaceHolder = getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CameraUtil.getInstance().doOpenCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        CameraUtil.getInstance().doStartPreview(surfaceHolder);
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(CameraUtil.PREVIEW_HAS_STARTED);
                }
            }, 1000);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraUtil.getInstance().doStopPreview();
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
}