package org.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import org.ui.activity.SelectImageActivity;

import java.util.ArrayList;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */


public class ImageSelector {

    // 返回选择图片列表的EXTRA_KEY  返回结果以Arraylist<String>的形式返回 对应的Filepath
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    // 最多可以选择多少张图片 - 默认8张
    private int mMaxCount = 9;
    // 选择图片的模式 - 默认多选
    private int mMode = SelectImageActivity.MODE_MULTI;
    // 是否显示拍照的相机
    private boolean mShowCamera = true;
    // 原始的图片
    private ArrayList<String> mOriginData;

    private ImageSelector() {
    }

    public static ImageSelector create() {
        return new ImageSelector();
    }

    /**
     * 单选模式
     */
    public ImageSelector single() {
        mMode = SelectImageActivity.MODE_SINGLE;
        return this;
    }

    /**
     * 多选模式
     */
    public ImageSelector multi() {
        mMode = SelectImageActivity.MODE_MULTI;
        return this;
    }

    /**
     * 设置可以选多少张图片
     */
    public ImageSelector count(int count) {
        mMaxCount = count;
        return this;
    }

    /**
     * 是否显示相机
     */
    public ImageSelector showCamera(boolean showCamera) {
        mShowCamera = showCamera;
        return this;
    }

    /**
     * 原来选择好的图片
     */
    public ImageSelector origin(ArrayList<String> originList) {
        this.mOriginData = originList;
        return this;
    }

    /**
     * 启动执行 权限6.0需要去申请
     */
    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addParamsByIntent(intent);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动执行 权限6.0需要去申请
     */
    public void start(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), SelectImageActivity.class);
        addParamsByIntent(intent);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 给Intent添加参数
     *
     * @param intent
     */
    private void addParamsByIntent(Intent intent) {
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT, mMaxCount);
        if (mOriginData != null && mMode == SelectImageActivity.MODE_MULTI) {
            intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData);
        }
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, mMode);
    }

}
