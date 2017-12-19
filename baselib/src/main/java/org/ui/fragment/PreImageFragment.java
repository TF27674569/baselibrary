package org.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.ui.activity.PreImageActivity;
import org.ui.view.photoview.PhotoView;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/3
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class PreImageFragment extends Fragment {

    private PhotoView mItemView;
    private Dialog mDialog;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemView = new PhotoView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mItemView.setLayoutParams(params);
        mItemView.enable();
        mItemView.enableRotate();
        return mItemView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            initData();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        initData();
    }

    private void initData() {
        if (isViewCreated && isUIVisible) {
            Bundle bundle = getArguments();
            String imagePath = bundle.getString(PreImageActivity.IMAGE_KEY);
            mDialog = android.app.ProgressDialog.show(getContext(), "请稍等", "正在加载图片...");
            mDialog.setCancelable(true);
            Glide.with(this)
                    .load(imagePath)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            mDialog.dismiss();
                            return false;
                        }
                    })
                    .into(mItemView);
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //页面销毁,恢复标记
        isViewCreated = false;
        isUIVisible = false;
    }
}
