package org.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselib.R;

import org.adapter.BaseFragmentAdapter;
import org.ui.fragment.PreImageFragment;
import org.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description：预览
 * <p/>
 * Created by TIAN FENG on 2017/11/3
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class PreImageActivity extends FragmentActivity implements View.OnClickListener {

    private static final String IMAGE_PATH = "IMAGE_PATH";
    public static final String IMAGE_KEY = "IMAGE_KEY";
    /**
     * 返回按钮
     */
    private ImageView mIvBackBtn;
    /**
     * 确认
     */
    private TextView mTvConfirm;
    private ViewPager mVpImage;
    /**
     * 显示个数
     */
    private TextView mTvCount;

    private String[] mImagePaths;

    private List<Fragment> mFragments;

    /**
     * 跳转页面
     *
     * @param imagePaths 图片的路径，或者url
     */
    public static void startActivity(Context context,String... imagePaths){
        Intent intent = new Intent(context,PreImageActivity.class);
        intent.putExtra(IMAGE_PATH,imagePaths);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_image);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mIvBackBtn = (ImageView) findViewById(R.id.ivBackBtn);
        mTvConfirm = (TextView) findViewById(R.id.tvConfirm);
        mVpImage = (ViewPager) findViewById(R.id.vpImage);
        mTvCount = (TextView) findViewById(R.id.tvCount);
    }

    private void initListener() {
        mIvBackBtn.setOnClickListener(this);
        mTvConfirm.setVisibility(View.INVISIBLE);
        mTvCount.setText("1/"+mImagePaths.length);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mImagePaths = getIntent().getStringArrayExtra(IMAGE_PATH);
        for (String imagePath : mImagePaths) {
            Fragment fragment = new PreImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IMAGE_KEY, imagePath);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        // 改变状态栏的颜色
        StatusBarUtils.statusBarTintColor(this, ContextCompat.getColor(this, R.color.title_color));
        mVpImage.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),mFragments));
        mVpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvCount.setText((position+1)+"/"+mImagePaths.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ivBackBtn){
            finish();
        }
    }
}
