package org.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.baselib.R;

import org.permission.Custom;
import org.permission.PermissionUtils;
import org.ui.camera.CameraGestureDetector;
import org.ui.camera.TakePicListener;
import org.utils.StatusBarUtils;
import org.ui.view.camera.CameraView;
import org.ui.view.camera.FaceView;
import org.ui.view.camera.util.CameraUtil;
import org.ui.view.camera.util.GoogleDetectListenerImpl;

import java.io.File;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class CameraActivity extends Activity implements Custom, View.OnClickListener, TakePicListener {
    private static final String TAG = "CameraActivity";
    private static final int PERMISSION_CODE = 0x1122;
    public static final int PREVIEW_CODE = 0x0122;

    private CameraView mCameraView;
    private FaceView mFaceView;
    private ImageButton mTakeBtn;
    private ImageView mCameraFocusInner;
    private ImageView mCameraFocusOuter;
    private FrameLayout mCameraFocusLayout;
    private ImageView mFlashIv;
    private ImageView mSwichCameraIv;
    private View mSettingRl;
    private int mWidth;
    private int mHeight;
    private GestureDetector mGestureDetector;
    private ProgressDialog mDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CameraUtil.PREVIEW_HAS_STARTED:
                    startGoogleDetect();
                    Log.e(TAG, "开启人脸识别");
                    break;
                case CameraUtil.RECEIVE_FACE_MSG:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Camera.Face[] faces = (Camera.Face[]) msg.obj;
                            mFaceView.setFaces(faces);
                            Log.e(TAG, "收到人脸识别的信息");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        StatusBarUtils.statusBarTranslucent(this);
        initView();
        initData();
        initListenre();
    }

    private void initView() {
        mCameraView = (CameraView) findViewById(R.id.camera_view);
        mFaceView = (FaceView) findViewById(R.id.face_view);
        mTakeBtn = (ImageButton) findViewById(R.id.take_btn);
        mCameraFocusInner = (ImageView) findViewById(R.id.camera_focus_inner);
        mCameraFocusOuter = (ImageView) findViewById(R.id.camera_focus_outer);
        mCameraFocusLayout = (FrameLayout) findViewById(R.id.camera_focus_layout);
        mFlashIv = (ImageView) findViewById(R.id.flash_iv);
        mSwichCameraIv = (ImageView) findViewById(R.id.swich_camera_iv);
        mSettingRl = findViewById(R.id.setting_rl);
    }

    private void initData() {
        // 预览之前首先需要 申请相机权限 和读写文件的权限
        PermissionUtils.with(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(this);
    }

    @Override
    public void onNext(boolean isSuccess) {
        if (!isSuccess) {
            finish();
            return;
        }
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mCameraFocusLayout.measure(w, h);
        mWidth = mCameraFocusLayout.getMeasuredWidth() / 2;
        mHeight = mCameraFocusLayout.getMeasuredHeight() / 2;
        mGestureDetector = new GestureDetector(new CameraGestureDetector(mCameraFocusLayout, mWidth, mHeight, mSettingRl));
    }


    private void initListenre() {
        // 拍照
        mTakeBtn.setOnClickListener(this);

        //  闪光灯
        mFlashIv.setOnClickListener(this);

        // 切换摄像头
        mSwichCameraIv.setOnClickListener(this);

        // 相机聚焦操作
        mCameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (CameraUtil.getInstance().getmCameraInfo().facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    return mGestureDetector.onTouchEvent(event);
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.take_btn) {// 拍照
            mDialog = android.app.ProgressDialog.show(this, "请稍等", "正在处理图片");
            CameraUtil.getInstance().doTakePic(this);
        } else if (viewId == R.id.flash_iv) {// 闪光灯
            CameraUtil.getInstance().setFlashMode(mFlashIv);
        } else if (viewId == R.id.swich_camera_iv) {// 切换摄像头
            changeCamera();
        }
    }

    // 获取到拍照的结果
    @Override
    public void onTakeSuccess(File file) {
        PreviewAvtivity.startActivity(this, file.getPath());
        mDialog.dismiss();
    }

    // 切换摄像头
    private void changeCamera() {
        CameraUtil.getInstance().doStopPreview();
        int newCameraId = (CameraUtil.getInstance().getCameraId() + 1) % 2;
        CameraUtil.getInstance().doOpenCamera(newCameraId);
        CameraUtil.getInstance().doStartPreview(mCameraView.getHolder());
        if (newCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mSwichCameraIv.setImageResource(R.drawable.camera_setting_switch_back);
            mFlashIv.setVisibility(View.VISIBLE);
            mFlashIv.setEnabled(true);
        } else {
            mSwichCameraIv.setImageResource(R.drawable.camera_setting_switch_front);
            mFlashIv.setVisibility(View.INVISIBLE);
            mFlashIv.setEnabled(false);
        }

    }

    // google 人脸识别api
    private void startGoogleDetect() {
        Camera.Parameters parameters = CameraUtil.getInstance().getCameraParaters();
        Camera camera = CameraUtil.getInstance().getCamera();
        if (parameters != null && parameters.getMaxNumDetectedFaces() > 0) {
            if (mFaceView != null) {
                mFaceView.clearFaces();
                mFaceView.setVisibility(View.VISIBLE);
            }
            camera.setFaceDetectionListener(new GoogleDetectListenerImpl(this, mHandler));
            camera.startFaceDetection();
        }
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionUtils.get().onRequestPermissionsResult(this, requestCode, permissions);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        CameraUtil.getInstance().doStopPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 权限申请成功之后开始预览画面
        mCameraView.openSurface();
        mCameraView.setHandler(mHandler);
    }


    // 释放资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraUtil.getInstance().onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PREVIEW_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
            CameraUtil.getInstance().doStopPreview();
            finish();
        }
    }
}
