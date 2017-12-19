package com.demo.config;

import android.os.Environment;

import java.io.File;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/6
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class PathConfig {

    public static final String DEX_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator+"fix"+ File.separator+"fix1.dex";

    public static final String SKIN_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator+"skin";
}
