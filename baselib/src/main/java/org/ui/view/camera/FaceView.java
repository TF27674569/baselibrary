package org.ui.view.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.baselib.R;

import org.ui.view.camera.util.CameraUtil;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

@SuppressLint("AppCompatCustomView")
public class FaceView extends ImageView {
    private Context mContext;
    private Camera.Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private boolean mirror;
    private Paint mLinePaint;

    private RectF rectF = new RectF();
    private Drawable mFaceIndicator = null;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        this.mContext = context;
        mFaceIndicator = mContext.getResources().getDrawable(R.drawable.ic_face_find_2);
    }

    public void setFaces(Camera.Face[] faces) {
        this.mFaces = faces;
        Log.d("Faceview", "invalidate");
//        ((View)getParent()).invalidate();
        invalidate();
        /*postInvalidate();
        invalidate();
        forceLayout();
        requestLayout();*/
    }
    public void clearFaces(){
        mFaces = null;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d("Faceview", "onDraw");
        if(mFaces == null || mFaces.length < 1){
            return;
        }
        if (mFaces != null) {
            Log.d("renlei","onDraw"+mFaces.length);
            int id = CameraUtil.getInstance().getCameraId();
            mirror = (id == Camera.CameraInfo.CAMERA_FACING_FRONT);
            canvas.save();
            prepareMatrix();
            mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针
            canvas.rotate(-0);   //Canvas.rotate()默认是逆时针
            for (int i = 0; i < mFaces.length; i++) {
                rectF.set(mFaces[i].rect);
                mMatrix.mapRect(rectF);
                mFaceIndicator.setBounds(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
                mFaceIndicator.draw(canvas);
            }
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    private void prepareMatrix() {
        mMatrix.setScale(mirror ? -1 : 1, 1);
        mMatrix.postRotate(9);
        mMatrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
        mMatrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		int color = Color.rgb(0, 150, 255);
        int color = Color.rgb(98, 212, 68);
//		mLinePaint.setColor(Color.RED);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(180);
    }
}
