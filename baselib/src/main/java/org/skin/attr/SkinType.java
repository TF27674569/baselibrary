package org.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.skin.SkinManager;
import org.skin.SkinResource;


/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public enum SkinType {
    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            // 获取skin资源
            SkinResource skinResource = getSkinResources();
            // 通过名称获取color
            ColorStateList color = skinResource.getColorleByName(resName);
            // color不为空 设置color
            if (color != null) {
                TextView textView = (TextView) view;
                textView.setTextColor(color);
            }
        }
    },
    BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            // 获取skin资源
            SkinResource skinResource = getSkinResources();
            // 通过名称获取drawable
            Drawable drawable = skinResource.getDrawableByName(resName);
            // 因为背景各个控件都可以设置所以捕获异常
            try{
                // drawable存在 设置图片
                if (drawable !=null){
                    ImageView imageView = (ImageView) view;
                    imageView.setImageDrawable(drawable);
                    return;
                }
                // 不存在则是color
                // 通过名称获取color
                ColorStateList color = skinResource.getColorleByName(resName);
                // color不为空 设置color
                if (color != null) {
                    view.setBackgroundColor(color.getDefaultColor());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    },
    SRC("src") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getSkinResources();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
            }
        }
    };


    /**
     * 获取皮肤资源
     */
    private static SkinResource getSkinResources() {
        return SkinManager.getInstance().getSkinResources();
    }


    /**
     * 资源加载方法
     *
     * @param view    加载控件
     * @param resName 资源名称 image_bg（eg： android:background="@mipmap/image_bg" ）
     */
    public abstract void skin(View view, String resName);


    /**
     * 资源的名称  background
     */
    private String mResName;

    SkinType(String resName) {
        this.mResName = resName;
    }

    public String getResName() {
        return mResName;
    }
}
