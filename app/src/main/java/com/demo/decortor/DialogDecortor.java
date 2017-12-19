package com.demo.decortor;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;

import com.demo.R;

import org.dialog.AlertDialog;
import org.http.mode.callback.BaseCallback;
import org.http.mode.base.AbsDecortor;
import org.http.mode.base.BaseCallbackAdapter;
import org.http.mode.params.HttpParams;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/27
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class DialogDecortor extends AbsDecortor implements Runnable {

    private Dialog mDialog;
    private Handler mHandler = new Handler();

    @Override
    public void execute(Context context, HttpParams params, BaseCallback callBack) {
        initDialog(context);
        super.execute(context, params, new BaseCallbackAdapter(callBack) {
            @Override
            public void onFinal(String url) {
                super.onFinal(url);
                mHandler.post(DialogDecortor.this);
            }
        });
    }

    private void initDialog(Context context) {
        mDialog = new AlertDialog.Builder(context)
                .setContentView(R.layout.dialog_loading)
                .show();
    }

    @Override
    public void run() {
        mDialog.dismiss();
    }
}
