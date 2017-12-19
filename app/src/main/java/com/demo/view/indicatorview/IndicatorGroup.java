package com.demo.view.indicatorview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/19.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class IndicatorGroup extends FrameLayout {

    public IndicatorGroup(@NonNull Context context) {
        this(context, null);
    }

    public IndicatorGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
