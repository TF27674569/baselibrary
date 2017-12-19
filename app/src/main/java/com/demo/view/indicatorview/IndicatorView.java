package com.demo.view.indicatorview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/19.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class IndicatorView extends HorizontalScrollView {
    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
