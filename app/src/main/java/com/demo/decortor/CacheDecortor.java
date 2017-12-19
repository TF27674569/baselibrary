package com.demo.decortor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.demo.bean.DaoData;
import com.demo.rx.RxPermission;

import org.dao.DaoSupportFactory;
import org.dao.IDaoSupport;
import org.http.mode.base.AbsDecortor;
import org.http.mode.base.BaseCallbackAdapter;
import org.http.mode.base.IHttpEngin;
import org.http.mode.callback.BaseCallback;
import org.http.mode.params.HttpParams;
import org.utils.MD5Util;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * description：缓存
 * <p/>
 * Created by TIAN FENG on 2017/11/27
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class CacheDecortor extends AbsDecortor {

    // 查询到数据后是否继续请求
    private Activity activity;

    // 数据库查询到的缓存
    private String mResult;


    public CacheDecortor() {
    }


    @Override
    public void addDecortor(IHttpEngin decortor) {
        super.addDecortor(decortor);
    }

    @Override
    public void execute(final Context context, final HttpParams params, final BaseCallback callBack) {
        RxPermission.create(activity = (Activity) context) // 请求权限
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(new Predicate<Boolean>() { // 判断是否授权成功
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        if (!aBoolean) { // 未授权不读数据库直接请求
                            onRequest(params, callBack);
                        }
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<IDaoSupport<DaoData>>>() { // 授权之后 读取数据库操作对象
                    @Override
                    public ObservableSource<IDaoSupport<DaoData>> apply(Boolean aBoolean) throws Exception {

                        return Observable.just(DaoSupportFactory.get().getDao(DaoData.class));
                    }
                })
                .map(new Function<IDaoSupport<DaoData>, List<DaoData>>() { // 发射查询到的数据给下游
                    @Override
                    public List<DaoData> apply(IDaoSupport<DaoData> daoDataIDaoSupport) throws Exception {
                        return daoDataIDaoSupport.querySupport().selection("url = ?").selectionArgs(MD5Util.string2MD5(params.getUrl())).query();
                    }
                })
                .map(new Function<List<DaoData>, DaoData>() { // 空值转换
                    @Override
                    public DaoData apply(List<DaoData> daoDatas) throws Exception {
                        if (daoDatas == null || daoDatas.size() == 0) {
                            return new DaoData();
                        }
                        return daoDatas.get(0);
                    }
                })
                .filter(new Predicate<DaoData>() { // 过滤空值
                    @Override
                    public boolean test(DaoData daoData) throws Exception {
                        if (TextUtils.isEmpty(daoData.url)) {
                            // 请求网络
                            onRequest(params, callBack);
                        }
                        return !TextUtils.isEmpty(daoData.url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DaoData>() {
                    @Override
                    public void accept(DaoData daoData) throws Exception {
                        // 回掉查询到的数据
                        callBack.onSuccess(params.getUrl(), mResult = daoData.json);
                        callBack.onFinal(params.getUrl());

                        onRequest(params, callBack);
                    }
                });
    }

    private void onRequest(final HttpParams params, final BaseCallback callBack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                decortor.execute(activity, params, new CacheCallback(callBack));
            }
        });
    }

    private class CacheCallback extends BaseCallbackAdapter {

        CacheCallback(BaseCallback callback) {
            super(callback);
        }

        @Override
        public void onSuccess(final String url, final String result) {
            // 如果查询到的和网络请求的不一致就继续回调，否则不回调
            if (!result.equals(mResult)) {
                super.onSuccess(url, result);
                RxPermission.create(activity) // 请求权限
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .filter(new Predicate<Boolean>() { // 判断是否成功
                            @Override
                            public boolean test(Boolean aBoolean) throws Exception {
                                return aBoolean;
                            }
                        })
                        .flatMap(new Function<Boolean, ObservableSource<IDaoSupport<DaoData>>>() { // 成功之后SQL工具
                            @Override
                            public ObservableSource<IDaoSupport<DaoData>> apply(Boolean aBoolean) throws Exception {
                                return Observable.just(DaoSupportFactory.get().getDao(DaoData.class));
                            }
                        })
                        .subscribe(new Consumer<IDaoSupport<DaoData>>() { // 操作SQl工具
                            @Override
                            public void accept(IDaoSupport<DaoData> daoDataIDaoSupport) throws Exception {
                                daoDataIDaoSupport.delete("url = ?", MD5Util.string2MD5(url));
                                daoDataIDaoSupport.insert(new DaoData(MD5Util.string2MD5(url), result));
                            }
                        });

            }
        }

    }
}
