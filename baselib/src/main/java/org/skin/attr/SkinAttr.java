package org.skin.attr;

import android.view.View;

/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SkinAttr {

    private SkinType mSkinType;
    private String mResName;

    public SkinAttr(SkinType skinType, String resName) {
        this.mSkinType = skinType;
        this.mResName = resName;
    }

    public void skin(View view){
        mSkinType.skin(view,mResName);
    }
}
