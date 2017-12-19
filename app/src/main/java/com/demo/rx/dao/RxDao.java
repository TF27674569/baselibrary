package com.demo.rx.dao;

import org.dao.DaoSupportFactory;
import org.dao.IDaoSupport;

import io.reactivex.Observable;

/**
 * description：
 * <p>
 * Created by TIAN FENG on 2017/12/14.
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class RxDao {

    public <T> Observable<IDaoSupport<T>> creat(final Class<T> tClass) {
        return Observable.just(DaoSupportFactory.get().getDao(tClass));
    }
}
