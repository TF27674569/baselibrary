package org.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.adapter.recycler.mode.EImageLoader;


/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/9/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class EHolder extends RecyclerView.ViewHolder{
    // 用来存放子View减少findViewById的次数 SparseArray 存放键值对为int->T类型 效率高于map
    private SparseArray<View> mViews;
    // 自定义图片加载器
    private static EImageLoader mImageLoader;

    static void setImageLoader(EImageLoader loader){
        mImageLoader = loader;
    }

    public EHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 设置TextView文本
     */
    public EHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    public EHolder setOnClick(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * 通过id获取view
     */
    public <T extends View> T getView(int viewId) {
        // 先从缓存中找
        View view = mViews.get(viewId);
        if (view == null) {
            // 直接从ItemView中找
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置View的Visibility
     */
    public EHolder setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 设置ImageView的资源
     */
    public EHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    /**
     * 设置条目点击事件
     */
    public void setOnIntemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    /**
     * 设置条目长按事件
     */
    public void setOnIntemLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }

    /**
     * 加载网络图片
     */
    public EHolder setImageByUrl(int viewId, String url) {
        ImageView imageView = getView(viewId);
        if (mImageLoader == null) {
            throw new NullPointerException("imageLoader is null,please call method setImageLoader(HolderImageLoader) by application!");
        }
        mImageLoader.displayImage(imageView.getContext(), imageView, url);
        return this;
    }

    /**
     * 设置图片通过路径，因为考虑加载图片的第三方可能不太一样
     */
    public EHolder setImageByUrl(int viewId, String path, EImageLoader imageLoader) {
        ImageView imageView = getView(viewId);
        if (imageLoader == null) {
            throw new NullPointerException("imageLoader is null!");
        }
        imageLoader.displayImage(imageView.getContext(), imageView, path);
        return this;
    }

}
