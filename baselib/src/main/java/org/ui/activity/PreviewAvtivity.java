package org.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.utils.StatusBarUtils;
import org.ui.view.photoview.PhotoView;

import java.io.File;

/**
 * description：拍照预览
 * <p/>
 * Created by TIAN FENG on 2017/11/2
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class PreviewAvtivity extends FragmentActivity implements View.OnClickListener {

    private PhotoView photoView;
    private Dialog dialog;
    private String filePath;
    private File file;
    private ImageView mIvBackBtn;
    /**
     * 预览
     */
    private TextView mTvTitle;
    /**
     * 确认
     */
    private TextView mTvConfirm;

    /**
     * 跳转
     */
    public static void startActivity(Activity activity, String imagePath) {
        Intent intent = new Intent(activity, PreviewAvtivity.class);
        intent.putExtra("imagePath", imagePath);
        activity.startActivityForResult(intent, CameraActivity.PREVIEW_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        StatusBarUtils.statusBarTintColor(this, ContextCompat.getColor(this, R.color.title_color));
        initView();
        initData();
    }

    private void initView() {
        mIvBackBtn = (ImageView) findViewById(R.id.ivBackBtn);
        mIvBackBtn.setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvTitle.setOnClickListener(this);
        mTvConfirm = (TextView) findViewById(R.id.tvConfirm);
        mTvConfirm.setOnClickListener(this);
        photoView = (PhotoView) findViewById(R.id.imageView);
        photoView.setOnClickListener(this);
    }

    private void initData() {
        filePath = getIntent().getStringExtra("imagePath");
        // 按返回键删除文件
        file = new File(filePath);
        dialog = ProgressDialog.show(this, "请稍等", "正在加载图片");
        Glide.with(this).load(filePath).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                dialog.dismiss();
                Toast.makeText(PreviewAvtivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                dialog.dismiss();
                return false;
            }
        }).into(photoView);
        photoView.enable();
        photoView.enableRotate();

    }

    @Override
    public void onBackPressed() {
        if (file.exists()) {
            file.delete();
        }
        super.onBackPressed();
    }

    public void back() {
        onBackPressed();
    }

    public void confirm() {
        setResult(Activity.RESULT_OK, getIntent().setData(Uri.fromFile(file)));
        finish();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivBackBtn) {
            back();
        }else if (i == R.id.tvConfirm) {
            confirm();
        }
    }
}
