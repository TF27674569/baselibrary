package org.skin;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import org.skin.attr.SkinAttr;
import org.skin.attr.SkinView;
import org.skin.callback.ISkinChangeListener;
import org.skin.support.SkinAppCompatViewInflater;
import org.skin.support.SkinSupport;
import org.skin.utils.SkinPerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 插件换肤基类
 * Created : TIAN FENG
 * Date : 2017/4/6
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class BaseSkinActivity extends AppCompatActivity implements LayoutInflaterFactory, ISkinChangeListener {

    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 拦截系统View创建 LayoutInflater是用来加载布局解析 xml文件
        LayoutInflater inflater = LayoutInflater.from(this);
        // 设置自己的LayoutInflaterFactory  在调用setContentView函数时 xml文件pull解析通过mFactory2回调onCreateView 函数 创建view
        LayoutInflaterCompat.setFactory(inflater,this);
        super.onCreate(savedInstanceState);
    }

    /**
     * 系统绘制界面时创建View所进入的方法
    */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 1. 获取创建的View
        View view = creatView(parent, name, context, attrs);
        // 2. 判断是否存在
        if (view != null){
            // 2.1 拿到Skin的属性
            List<SkinAttr> skinAttrs = SkinSupport.getSkinAttrs(context,attrs);
            // 2.2 存在我们将其加入到管理类
            if (skinAttrs.size() != 0) {
                // 2.2.1 创建一个SkinView
                SkinView skinView = new SkinView(view, skinAttrs);
                // 2.2.2 管理SkinView
                managerSkinView(skinView);
                // 2.2.2 判断有没有读取资源包
                judgeChangeSkin(skinView);
            }

        }


        return view;
    }


    /**
     * 管理SkinView
     */
    private void managerSkinView(SkinView skinView) {
        // 1. 拿到SkinView的集合
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        // 2.2 判断是否为空
        if (skinViews == null) {
            // 2.2.1 为空创建一个新的
            skinViews = new ArrayList<>();
            // 2.2. 注册添加
            SkinManager.getInstance().registerSkinView(this,skinViews);
        }
        // 添加到集合
        skinViews.add(skinView);
    }

    /**
     * 判断有没有加载过皮肤
     */
    private void judgeChangeSkin(SkinView skinView) {
        // 1.加载路径
        String skinPath = SkinPerUtils.getInstance(this).getSkinPath();
        // 2. 是否加载过
        boolean isLoadSkin = !TextUtils.isEmpty(skinPath);
        // 3. 判断是否加载过
        if (isLoadSkin){
            // 4. 加载过直接加载
            skinView.skin();
        }
    }


    /**
     * 创建View 拷贝系统源码
     */
    private View creatView(View parent, String name, Context context, AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    /**
     * 系统源码
     */
    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }


    /**
     * 换肤回调接口
     */
    @Override
    public void changeSkin(SkinResource skinResource) {
        // 自定义控件换肤实现 子类复写需要换肤的自定义view
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().onDestroy(this);
        super.onDestroy();
    }
}
