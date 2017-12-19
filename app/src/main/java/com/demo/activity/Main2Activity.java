package com.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.base.NavigationTitle;
import com.demo.view.bezier.WaveView;

import org.annotation.CheckNet;
import org.annotation.Event;
import org.annotation.ViewById;
import org.base.BaseActivity;
import org.http.HttpUtils;
import org.http.mode.callback.Callback;
import org.utils.PreferencesUtil;

import java.io.File;


public class Main2Activity extends BaseActivity {

    private String mUrl = "http://www.eoemarket.com/download/771294_360";
    private static String mPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "downLoad/磁力链接.apk";
    private static final String IS_DOWNLOAD_OVER = "IS_DOWNLOAD_OVER";

    //下载
    @ViewById(R.id.downLoadbtn)
    public Button downLoadbtn;

    @ViewById(R.id.waveView)
    public WaveView waveView;

    //进度
    @ViewById(R.id.tvProgress)
    public TextView tvProgress;


    private File mFile;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void initView() {
        new NavigationTitle.Builder(this)
                .setTitle("Main2Activity")
                .immersed()
                .setBackClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .build();

    }

    @Override
    protected void initData() {
        waveView.setStartYRange(0);
        waveView.startAnimation();

        mFile = new File(mPath);

        if (mFile.isFile() && mFile.length() > 0) {
            // 是否下载结束
            boolean isDownLoadOvewr = PreferencesUtil.get(IS_DOWNLOAD_OVER, false);
            //是否下载结束
            if (isDownLoadOvewr) {//已经下载完毕
                downLoadbtn.setText("安装");
            } else {
                downLoadbtn.setText("继续");
            }
        }


    }


    @CheckNet
    @Event(R.id.downLoadbtn)
    public void downLoadbtnClick() {
//        if (downLoadbtn.getText().toString().equals("安装")){
//            // 跳安装
//            installApk(mFile);
//            return;
//        }
        HttpUtils.with(this)
                .url(mUrl)
                .tag(this)
                .path(mPath)
//                .autoResume()
                .downLoad(new Callback.ProgressCallback<File>() {
                    @Override
                    public void onProgress(long total, long current, float progress) {
                        if (progress < 0) {
                            progress = 0;
                        } else if (progress > 1) {
                            progress = 1;
                        }
                        waveView.setStartYRange(progress);
                        tvProgress.setText("进度：" + ((int) current * 1f / total * 100) + "%");
                    }


                    @Override
                    public void onSuccess(String url, File result) {
                        Toast.makeText(Main2Activity.this, result.getPath(), Toast.LENGTH_SHORT).show();
                        PreferencesUtil.put(IS_DOWNLOAD_OVER, true);
                        downLoadbtn.setText("安装");
                        installApk(result);
                    }

                    @Override
                    public void onError(String url, Throwable e) {
                        Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinal(String url) {
                        Toast.makeText(Main2Activity.this, "end", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 跳转系统安装
     */
    public void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "cn.bingoogolapple.update.demo.fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            startActivity(intent);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        HttpUtils.cancle(this);
    }

    @Override
    protected void onDestroy() {
        HttpUtils.cancle(this);
        super.onDestroy();
    }
}
