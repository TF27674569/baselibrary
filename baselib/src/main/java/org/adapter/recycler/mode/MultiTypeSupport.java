package org.adapter.recycler.mode;

/**
 * Description : 多布局适配接口
 * <p/>
 * Created : TIAN FENG
 * Date : 2017/6/29
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
     int getLayoutId(T item, int position);
}
