package org.ui.camera;

import android.hardware.Camera;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.ui.view.camera.util.CameraUtil;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class CameraGestureDetector implements GestureDetector.OnGestureListener {

    private FrameLayout mFrameLayout;
    private int mWidth,mHeight;
    private View mRelativeLayout;

    public CameraGestureDetector(FrameLayout focusLayout,int width ,int height ,View settingRl ) {
        this.mFrameLayout = focusLayout;
        this.mWidth = width;
        this.mHeight = height;
        this.mRelativeLayout = settingRl;
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        CameraUtil.getInstance().autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    Log.d("renlei", "聚焦成功");
                } else {
                    Log.d("renlei", "聚焦失败");

                }
                mFrameLayout.setVisibility(View.GONE);
            }
        });
        CameraUtil.getInstance().setFocusArea(mFrameLayout.getContext(), e);
        showFocusIcon(e);
        return true;
    }

    private void showFocusIcon(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFrameLayout.getLayoutParams();
        params.leftMargin = (int) (x - mWidth + 0.5);
        params.topMargin = (int) (y - mHeight + 0.5 /*+ mRelativeLayout.getHeight()*/);
        mFrameLayout.requestLayout();
        mFrameLayout.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mFrameLayout.getLayoutParams();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
