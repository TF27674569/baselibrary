package com.demo.view.indicator;

import android.view.View;
import android.view.ViewGroup;

/**
 * description： 指示器的Adapter
 * <p/>
 * Created by TIAN FENG on 2017/8/13 20:39 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
public abstract class IndicatorAdapter<T extends View> {
	// 获取总共的显示条数
	public abstract int getCount();

	// 根据当前的位置获取View
	public abstract T getView(int position, ViewGroup parent);

	// 高亮当前位置
	public void highLightIndicator(int position, T view) {

	}

	// 重置当前位置
	public void restoreIndicator(int position, T view) {

	}

	// 添加底部跟踪的指示器
	public View getBottomTrackView(int itemWidth) {
		return null;
	}
}
