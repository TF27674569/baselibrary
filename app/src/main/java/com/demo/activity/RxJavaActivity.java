package com.demo.activity;

import android.Manifest;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.demo.R;
import com.demo.bean.DaoData;
import com.demo.bean.FoodModel;
import com.demo.config.DataFactory;
import com.demo.rx.RxPermission;

import org.annotation.Event;
import org.base.BaseActivity;
import org.dao.DaoSupportFactory;
import org.dao.IDaoSupport;
import org.log.L;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/11.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class RxJavaActivity extends BaseActivity {

    private static final String TAG = "RxJavaActivity";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_rxjava);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }


    @Event(R.id.btnSkip)
    public void btnSkipClick(Button btnSkip) {
        /**
         * skip 过滤前面三项
         */
        Observable.just(1, 2, 4, 6, 0, 5, 7, 8, 9)
                .skip(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "");
                    }
                });
    }

    @Event(R.id.btnFilter)
    public void btnFilterClick(Button btnFilter) {
        /**
         * filter 指定判断条件过滤
         */

        Observable.just(1, 2, 4, 6, 0, 5, 7, 8, 9)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 4;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "");
                    }
                });
    }

    @Event(R.id.btnDistinct)
    public void btnDistinctClick(Button btnDistinct) {
        /**
         * distinct操作符的用处就是用来去重，所有重复的数据都会被过滤掉(会保留一位)。
         */
        Observable.just(1, 2, 1, 6, 1, 1, 7, 8, 9)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "");
                    }
                });
    }

    @Event(R.id.btnScan)
    public void btnScanClick(Button btnScan) {
        /**
         *  扫描连续的两个数据
         *  先扫描 12  然后 21 ， 16 ， 61  11  17  78  89
         */
        Observable.just(1, 2, 1, 6, 1, 1, 7, 8, 9)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.e(TAG, "integer = " + integer + ", integer2 = " + integer2);
                        return integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "");
                    }
                });
    }

    @Event(R.id.btnFlatMap)
    public void btnFlatMapClick(Button btnFlatMap) {
        List<FoodModel> mode = DataFactory.factoryFoods();
        Observable.fromArray(mode.toArray(new FoodModel[]{}))
                .flatMap(new Function<FoodModel, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(FoodModel foodModel) throws Exception {
                        return null;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, s);
                    }
                });

    }


    @Event(R.id.btnInsect)
    public void btnInsectClick(Button btnInsect) {
        RxPermission.create(this) .request(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .filter(new Predicate<Boolean>() { // 成功才往下走
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        L.e("申请权限" + aBoolean);
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<IDaoSupport<DaoData>>>() { // 权限申请成功之后 需要创建一个数据库操作对象
                    @Override
                    public ObservableSource<IDaoSupport<DaoData>> apply(Boolean aBoolean) throws Exception {
                        L.e("权限申请成功之后 需要创建一个数据库操作对象");
                        return Observable.just(DaoSupportFactory.get().getDao(DaoData.class));
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<IDaoSupport<DaoData>>() {
                    @Override
                    public void accept(IDaoSupport<DaoData> daoDataIDaoSupport) throws Exception {
                        DaoData daoData = new DaoData();
                        daoData.url = "10086";
                        daoData.json = "";
                        daoDataIDaoSupport.insert(daoData);
                        L.e("插入比较耗时");
                    }
                });
    }

    @Event(R.id.btnQuery)
    public void btnQueryClick(Button btnQuery) {

        RxPermission.create(this) // 请求权限
                .request(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .filter(new Predicate<Boolean>() { // 成功才往下走
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        L.e("申请权限" + aBoolean);
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<IDaoSupport<DaoData>>>() { // 权限申请成功之后 需要创建一个数据库操作对象
                    @Override
                    public ObservableSource<IDaoSupport<DaoData>> apply(Boolean aBoolean) throws Exception {
                        L.e("权限申请成功之后 需要创建一个数据库操作对象");
                        return Observable.just(DaoSupportFactory.get().getDao(DaoData.class));
                    }
                })
                .flatMap(new Function<IDaoSupport<DaoData>, ObservableSource<DaoData>>() { // 发射所有查到的数据
                    @Override
                    public ObservableSource<DaoData> apply(IDaoSupport<DaoData> daoDataIDaoSupport) throws Exception {
                        List<DaoData> daos = daoDataIDaoSupport.querySupport().selection("url = ?").selectionArgs("10086").query();
                        L.e("查询数据");
                        return Observable.fromIterable(daos);
                    }
                })
                .filter(new Predicate<DaoData>() { // 下游只需要10086的url
                    @Override
                    public boolean test(DaoData daoData) throws Exception {
                        L.e("需要10086的url");
                        return daoData == null || daoData.url.equals("10086");
                    }
                })
                .map(new Function<DaoData, String>() { // 下游只需要10086的json
                    @Override
                    public String apply(DaoData daoData) throws Exception {
                        L.e("需要10086的json");
                        if (daoData == null) {
                            daoData = new DaoData();
                            daoData.json = "56456464";
                        }
                        return daoData.json;
                    }
                })
                .filter(new Predicate<String>() { // 10086的json 如果为空就请求网络不为空就操作json 不请求网络
                    @Override
                    public boolean test(String s) throws Exception {
                        L.e("判断json是否符合条件");
                        return TextUtils.isEmpty(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        L.e("请求网络");
                    }
                });
    }
}
