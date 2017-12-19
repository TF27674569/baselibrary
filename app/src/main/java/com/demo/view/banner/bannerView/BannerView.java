package com.demo.view.banner.bannerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.R;
import com.demo.view.banner.bannerpager.BannerViewPager;
import com.demo.view.banner.os.BannerObsever;


/**
 * description：轮播
 * <p/>
 * Created by TIAN FENG on 2017/5/10 12:14
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BannerView extends FrameLayout implements BannerObsever, ViewPager.OnPageChangeListener {
    public static String TAG = "BannerView";
    // 适配器
    private BannerAdapter mBannerAdapter;
    // 无限轮播的pager
    private BannerViewPager mBannerViewPager;
    // 指示器原点容器
    private LinearLayout mIndicatorGroup;
    // 广告位
    private TextView mDescriptionTv;
    // 整个指示器的父布局
    private FrameLayout mBottomGroup;
    // 当前位置
    private int mCurrentPosition;
    // 指示器位置
    private int mIndicatorGravity;
    // 广告位置
    private int mDescriptionGravity;
    // 广告文本大小
    private int mDescriptionTextSize;
    // 广告文字颜色
    private int mDescriptionTextColor;
    // 底部指示器颜色
    private int mBottomColor;
    // 指示器（小圆点）间距
    private int mIndicatorMargin;
    // 底部容器高
    private int mBottomHeight;


    public BannerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_banner, this);
        initBannerView(context, attrs);
        initView();
    }


    private void initBannerView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mIndicatorGravity = array.getInt(R.styleable.BannerView_indicator_gravity, 0);
        mDescriptionGravity = array.getInt(R.styleable.BannerView_description_gravity, -1);
        mDescriptionTextSize = (int) array.getDimension(R.styleable.BannerView_description_text_size, 12);
        mDescriptionTextColor = array.getColor(R.styleable.BannerView_description_text_color, Color.WHITE);
        mBottomColor = array.getColor(R.styleable.BannerView_bottom_color, Color.parseColor("#50000000"));
        int margin = (int) array.getDimension(R.styleable.BannerView_indicator_margin, 0);
        mBottomHeight = dip2px((int) array.getDimension(R.styleable.BannerView_bottom_height,15));
        mIndicatorMargin = dip2px(margin);
        array.recycle();
    }


    /**
     * 初始化bannerView
     */
    private void initView() {
        mBottomGroup = (FrameLayout) findViewById(R.id.bottom_group);
        mBannerViewPager = (BannerViewPager) findViewById(R.id.banner_pager_layout);
        mIndicatorGroup = (LinearLayout) findViewById(R.id.indicator_group);
        mDescriptionTv = (TextView) findViewById(R.id.description_tv);
        mBottomGroup.setBackgroundColor(mBottomColor);
        mDescriptionTv.setTextColor(mDescriptionTextColor);
        mDescriptionTv.setTextSize(mDescriptionTextSize);
        int gravity = -1;
        switch (mIndicatorGravity) {
            case -1:
                gravity = Gravity.LEFT|Gravity.CENTER;
                break;
            case 0:
                gravity = Gravity.CENTER;
                break;
            case 1:
                gravity = Gravity.RIGHT|Gravity.CENTER;
                break;
        }
        mIndicatorGroup.setGravity(gravity);

        switch (mDescriptionGravity) {
            case -1:
                gravity = Gravity.LEFT|Gravity.CENTER;
                break;
            case 0:
                gravity = Gravity.CENTER;
                break;
            case 1:
                gravity = Gravity.RIGHT|Gravity.CENTER;
                break;
        }
        mDescriptionTv.setGravity(gravity);

        mBottomGroup.getLayoutParams().height = mBottomHeight;
    }


    /***************************外界调用常用方法*****************************************************************************************************/

    /**
     * 设置适配器
     */
    public void setAdapter(BannerAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        this.mBannerAdapter = adapter;
        // 设置bannerViewpagerd的适配器
        mBannerViewPager.setAdapter(adapter);
        adapter.registerBannerObsever(this);
        initIndicator();
        // 联动指示器数据
        mBannerViewPager.addOnPageChangeListener(this);
        if (adapter.getCount()>1){
            stopScroll();
        }
    }

    // 开启滚动
    public void startScroll() {
        if (mBannerAdapter != null) {
            mBannerViewPager.startScroll();
        }
    }

    //停止滚动
    public void stopScroll() {
        if (mBannerAdapter != null) {
            mBannerViewPager.stopScroll();
        }
    }

    // 设置滚动中的duration
    public void setDuration(int duration) {
        mBannerViewPager.setDuration(duration);
    }

    // 设置切换间隔时间
    public void setCutDownTime(int cutDownTime) {
        mBannerViewPager.setCutDownTime(cutDownTime);
    }

    //获取当前的标准位置
    public int getCurrentPosition() {
        return mBannerViewPager.getCurrentPosition();
    }

    /********************************************************************************************************************************************/

    /**
     * 初始化指示器
     */
    private void initIndicator() {
        mIndicatorGroup.removeAllViews();
        // 显示指示器个数
        int count = mBannerAdapter.getCount();
        if (count <= 0) {
            return;
        }
        View indicatorView = null;
        // 添加指示器
        for (int i = 0; i < count; i++) {
            // 拿到指示器控件
            indicatorView = mBannerAdapter.getIndicatorView(mBottomHeight);
            if (indicatorView == null) {
                mIndicatorGroup.setVisibility(GONE);
                break;
            }
            indicatorView.setPadding(0, 0, mIndicatorMargin, 0);
            mIndicatorGroup.setVisibility(VISIBLE);
            mIndicatorGroup.addView(indicatorView);
            // 设置默认亮度
            mBannerAdapter.setDefaultLight(i, indicatorView);
        }
        // 广告详情
        String description = mBannerAdapter.getDescription(mCurrentPosition);
        if (indicatorView == null && TextUtils.isEmpty(description)) {
            mBottomGroup.setVisibility(GONE);
        } else {
            mBottomGroup.setVisibility(VISIBLE);
        }
        if (indicatorView != null) {
            // 解决添加数据notify后的位置错乱
            mCurrentPosition = (mBannerViewPager.getCurrentPosition() + 1) % mBannerAdapter.getCount() - 1;

            if (mCurrentPosition<0){
                mCurrentPosition = 0;
            }

            // 当前位置高亮
            mBannerAdapter.setHightLight(mCurrentPosition, mIndicatorGroup.getChildAt(mCurrentPosition));
        }
        // 广告详情
        mDescriptionTv.setText(description);
    }


    /**
     * 当数据发生改变时进入,不用唤醒BannViewPager的状态 adapter已经做了唤醒
     * 这里需要重新绘制指示器和广告详情
     */
    @Override
    public void onChanged() {
        initIndicator();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        int pos = position % mBannerAdapter.getCount();
        Log.e(TAG, "pos==" + pos);
        mBannerAdapter.setDefaultLight(mCurrentPosition, mIndicatorGroup.getChildAt(mCurrentPosition));
        mCurrentPosition = pos;
        mBannerAdapter.setHightLight(mCurrentPosition, mIndicatorGroup.getChildAt(mCurrentPosition));
        mDescriptionTv.setText(mBannerAdapter.getDescription(mCurrentPosition));
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }
}
