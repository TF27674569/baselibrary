package com.demo.bean;

import org.dao.annotation.Column;
import org.dao.annotation.Table;

import java.io.Serializable;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/27
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
@Table
public class DaoData implements Serializable {

    @Column
    public String url;

    @Column
    public String json;

    public DaoData(){

    }

    public DaoData(String url, String json) {
        this.url = url;
        this.json = json;
    }
}
