package com.demo.view.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/12/4
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

@SuppressLint("AppCompatCustomView")
public class ShoppingView extends TextView implements ValueAnimator.AnimatorUpdateListener {

    public static final int VIEW_SIZE = 20;

    protected Context mContext;
    protected Paint mPaint4Circle;
    protected int radius;

    protected Point startPosition;
    protected Point endPosition;


    public ShoppingView(Context context) {
        this(context, null);
    }

    public ShoppingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPaint4Circle = new Paint();
        mPaint4Circle.setColor(Color.RED);
        mPaint4Circle.setAntiAlias(true);


        setGravity(Gravity.CENTER);
        setText("1");
        setTextColor(Color.WHITE);
        setTextSize(12);
    }

    // 设置起点
    public void setStartPosition(Point startPosition) {
        // 高度减去大小的一半
        startPosition.y -= VIEW_SIZE/2;
        this.startPosition = startPosition;
    }

    // 设置终点
    public void setEndPosition(Point endPosition) {
        this.endPosition = endPosition;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int PX4SIZE = (int) convertDpToPixel(VIEW_SIZE, mContext);
        setMeasuredDimension(PX4SIZE, PX4SIZE);
        radius = PX4SIZE / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画一个文本
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint4Circle);
        super.onDraw(canvas);
    }

    // 开始启动动画
    public void startBeizerAnimation() {
        if (startPosition == null || endPosition == null) return;

        // 控制点X坐标
        int pointX = (startPosition.x + endPosition.x) / 2;
        // 控制点Y坐标
        int pointY = (int) (startPosition.y - convertDpToPixel(100, mContext));
        // 控制点
        Point controllPoint = new Point(pointX, pointY);
        // 贝塞尔估值器
        BezierEvaluator bezierEvaluator = new BezierEvaluator(controllPoint);
        // 动画
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, startPosition, endPosition);
        // 监听动画
        anim.addUpdateListener(this);
        // 持续时间
        anim.setDuration(400);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.removeView(ShoppingView.this);
            }
        });
        // 插值器
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        // 获取估值器计算的值
        Point point = (Point) animation.getAnimatedValue();
        // 重设位置
        setX(point.x);
        setY(point.y);
        // 重绘
        invalidate();
    }


    public class BezierEvaluator implements TypeEvaluator<Point> {

        private Point controllPoint;

        public BezierEvaluator(Point controllPoint) {
            this.controllPoint = controllPoint;
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {
            int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
            int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
            return new Point(x, y);
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}

