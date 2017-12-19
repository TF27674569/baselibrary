package org.ui.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.R;
import com.bumptech.glide.Glide;

import org.ui.TackCamera;
import org.ui.ImageSelector;
import org.adapter.recycler.EAdapter;
import org.adapter.recycler.EHolder;
import org.adapter.recycler.mode.ItemBind;
import org.utils.StatusBarUtils;

import java.util.ArrayList;

/**
 * description：图片选择页面
 * <p/>
 * Created by TIAN FENG on 2017/11/3
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class SelectImageActivity extends Activity implements View.OnClickListener {
    // 是否显示相机的EXTRA_KEY
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    // 总共可以选择多少张图片的EXTRA_KEY
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    // 原始的图片路径的EXTRA_KEY
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 选择模式的EXTRA_KEY
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";


    // 加载所有的数据
    private static final int START_CAMERA = 0x0021;

    // 加载所有的数据
    private static final int LOADER_TYPE = 0x0021;
    // 选择图片的模式 - 多选
    public static final int MODE_MULTI = 0x0011;
    // 选择图片的模式 - 单选
    public static int MODE_SINGLE = 0x0012;
    // 单选或者多选，int类型的type
    private int mMode = MODE_MULTI;
    // int 类型的图片张数
    private int mMaxCount = 8;
    // 返回按钮
    private ImageView mIvBackBtn;
    /**
     * 预览
     */
    private TextView mTvTitle;
    /**
     * 确认
     */
    private TextView mTvConfirm;
    private RecyclerView mRvImageList;
    /**
     * 预览
     */
    private TextView mSelectPreview;
    private TextView mSelectNum;
    /**
     * 确认
     */
    private TextView mSelectFinish;
    // boolean 类型的是否显示拍照按钮
    private boolean mShowCamera = true;

    // ArraryList<String> 已经选择好的图片
    private ArrayList<String> mResultList;

    private EAdapter mAdapter;

    // 查询到的图片
    private ArrayList<String> mImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mIvBackBtn = (ImageView) findViewById(R.id.ivBackBtn);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mTvConfirm = (TextView) findViewById(R.id.tvConfirm);
        mRvImageList = (RecyclerView) findViewById(R.id.rvImageList);
        mSelectPreview = (TextView) findViewById(R.id.select_preview);
        mSelectNum = (TextView) findViewById(R.id.select_num);
        mSelectFinish = (TextView) findViewById(R.id.select_finish);
        mTvConfirm.setVisibility(View.INVISIBLE);
        mTvTitle.setText("所有图片");
        // 改变状态栏的颜色
        StatusBarUtils.statusBarTintColor(this, ContextCompat.getColor(this, R.color.title_color));
    }

    private void initData() {

        // 获取传递过来的参数
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }

        // 初始化本地图片数据
        initImageList();

        // 改变显示
        exchangeViewShow();
    }

    private void initImageList() {
        getLoaderManager().initLoader(LOADER_TYPE, null, mLoaderCallback);
    }

    private void exchangeViewShow() {
        // 预览是不是可以点击，显示什么颜色
        if (mResultList.size() > 0) {
            // 至少选择了一张
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);
        } else {
            // 一张都没选
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }

        // 中间图片的张数也要显示
        mSelectNum.setText(mResultList.size() + "/" + mMaxCount);
    }

    private void initListener() {
        mIvBackBtn.setOnClickListener(this);
        mSelectPreview.setOnClickListener(this);
        mSelectFinish.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivBackBtn) {
            finish();
        } else if (i == R.id.select_preview) {
            PreImageActivity.startActivity(this, mResultList.toArray(new String[]{}));
        } else if (i == R.id.select_finish) {
            // 选择好的图片传过去
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ImageSelector.EXTRA_RESULT, mResultList);
            setResult(RESULT_OK, intent);
            // 关闭当前页面
            finish();
        }
    }

    /**
     * 加载图片的CallBack
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                private final String[] IMAGE_PROJECTION = {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media._ID};

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // 查询
                    CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                                    + IMAGE_PROJECTION[3] + "=? ",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                    return cursorLoader;
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    // 只保存String路径
                    if (data != null && data.getCount() > 0) {
                        mImages = new ArrayList<>();

                        // 如果需要显示拍照，就在第一个位置上加一个空String
                        if (mShowCamera) {
                            mImages.add("");
                        }

                        // 不断的遍历循环
                        while (data.moveToNext()) {
                            // 只保存路径
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            mImages.add(path);
                        }

                        // 显示列表数据
                        showImageList();
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };

    private void showImageList() {
        mAdapter = EAdapter.Builder.load(mImages)
                // 每行三个
                .manager(new GridLayoutManager(this, 3))
                .item(R.layout.item_select_image)
                .bind(new ItemBind<String>() {
                    @Override
                    public void convert(EHolder holder, final String item, int position, final RecyclerView.Adapter adapter) {
                        if (TextUtils.isEmpty(item)) {
                            // 显示拍照
                            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
                            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
                            holder.setViewVisibility(R.id.image, View.INVISIBLE);

                            holder.setOnIntemClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 拍照
                                    TackCamera.with(SelectImageActivity.this).requestCode(START_CAMERA).start();
                                }
                            });
                        } else {
                            // 显示图片
                            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
                            holder.setViewVisibility(R.id.media_selected_indicator, View.VISIBLE);
                            holder.setViewVisibility(R.id.image, View.VISIBLE);

                            // 显示图片
                            ImageView imageView = holder.getView(R.id.image);
                            Glide.with(SelectImageActivity.this).load(item)
                                    .centerCrop().into(imageView);

                            final ImageView selectIndicatorIv = holder.getView(R.id.media_selected_indicator);

                            if (mResultList.contains(item)) {
                                // 点亮选择勾住图片
                                selectIndicatorIv.setSelected(true);
                            } else {
                                selectIndicatorIv.setSelected(false);
                            }

                            // 给条目增加点击事件
                            holder.setOnIntemClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // 没有就加入集合，有就移除集合
                                    if (!mResultList.contains(item)) {

                                        // 不能大于最大的张数
                                        if (mResultList.size() >= mMaxCount) {
                                            // Toast
                                            Toast.makeText(SelectImageActivity.this, "最多只能选取" + mMaxCount + "张图片", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        mResultList.add(item);
                                        // 点亮选择勾住图片
                                        selectIndicatorIv.setSelected(true);
                                    } else {
                                        mResultList.remove(item);
                                        // 取消选择勾住图片
                                        selectIndicatorIv.setSelected(false);
                                    }

                                    // 通知显示布局
                                    exchangeViewShow();
                                }
                            });
                        }
                    }
                })
                .into(mRvImageList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == START_CAMERA) {
            Uri uri = data.getData();
            if (mShowCamera){
                mImages.add(1,uri.getPath());
            }else {
                mImages.add(0,uri.getPath());
            }
            // 唤醒数据
            mAdapter.notifyDataSetChanged();
            // 发送广播 下次进入,可在相册拿到
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }
}
