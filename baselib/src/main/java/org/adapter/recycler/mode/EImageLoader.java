package org.adapter.recycler.mode;

import android.content.Context;
import android.widget.ImageView;

/**
 * description：第三方图片加载规则
 * <p/>
 * Created by TIAN FENG on 2017/9/30
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public interface EImageLoader {
    void displayImage(Context context, ImageView imageView, String imagePath);
}
