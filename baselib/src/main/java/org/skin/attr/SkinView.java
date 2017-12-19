package org.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class SkinView {
    private List<SkinAttr> mSkinAttrs;
    private View mView;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    public void skin(){
        for (SkinAttr attr : mSkinAttrs) {
            attr.skin(mView);
        }
    }
}
