package org.adapter.recycler.mode;


import android.support.v7.widget.RecyclerView;

import org.adapter.recycler.EHolder;

/**
 * description： 绑定规则接口
 * <p/>
 * Created by TIAN FENG on 2017/9/29
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface ItemBind<T> {
    void convert(EHolder holder, T itemData, int position,  RecyclerView.Adapter adapter);
}
