package com.demo.recycler.zuimei;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/7.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class ZuiMeiLayoutManager extends BaseLinearLayoutManager {

    public ZuiMeiLayoutManager(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    protected void rule(View view, LayoutState layoutState, LayoutChunkResult result, RecyclerView.LayoutParams params) {
        int left, top, right, bottom;
        if (isLayoutRTL()) {
            right = getWidth() - getPaddingRight();
            left = right - mOrientationHelper.getDecoratedMeasurementInOther(view);
        } else {
            left = getPaddingLeft();
            right = left + mOrientationHelper.getDecoratedMeasurementInOther(view);
        }
        if (layoutState.mLayoutDirection == LayoutState.LAYOUT_START) {
            bottom = layoutState.mOffset;
            top = layoutState.mOffset - result.mConsumed;
        } else {
            top = layoutState.mOffset;
            bottom = layoutState.mOffset + result.mConsumed;
        }

        layoutDecoratedWithMargins(view, left, top, right, bottom);

        if (params.isItemRemoved() || params.isItemChanged()) {
            result.mIgnoreConsumed = true;
        }
        result.mFocusable = view.hasFocusable();
    }
}
