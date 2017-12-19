package org.http.mode.params;

import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * description：请求参数封装
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class HttpParams implements ContentTypeParams.MultipartType {
    // 地址
    private String mUrl;
    // data
    private String mData;
    // tag
    private Object mTag;
    // 请求方式
    private int mMethod = HttpConfig.GET;
    // post上传类型 默认form-data，如果含有文件，会使用multipart/form-data
    private String mContentType = HttpConfig.FROM_DATA;
    // 请求头
    private Map<String, String> mHeader;
    // 请求参数
    private Map<String, String> mParams;
    // 带类型的请求参数
    private Set<ContentTypeParams> mTypeParams;
    // 是否断点续传
    private boolean mIsAutoResume = false;
    // 文件路径
    private String mPath = Environment.getExternalStorageDirectory().getPath();
    // 失败重试
    private boolean mIsRetry = true;
    // 超時设定
    private TimeOuts mTimeOuts;

    public HttpParams() {
        mHeader = new HashMap<>();
        mParams = new HashMap<>();
        mTypeParams = new HashSet<>();
        mTimeOuts = new TimeOuts();
    }


    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        if (path.contains(mPath)) {
            this.mPath = path;
        } else {
            this.mPath = mPath + File.separator + path;
        }
    }

    public void setAutoResume(boolean autoResume) {
        mIsAutoResume = autoResume;
    }

    public boolean isAutoResume() {
        return mIsAutoResume;
    }

    public void setMethod(int method) {
        mMethod = method;
    }

    public void setReadTimeOut(int readTimeOut) {
        mTimeOuts.readTimeOut = readTimeOut;
    }

    public void setWriteTimeOut(int writeTimeOut) {
        mTimeOuts.writeTimeOut = writeTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        mTimeOuts.connectTimeOut = connectTimeOut;
    }

    public void setRetryOnConnectionFailure(boolean isRetry) {
        mIsRetry = isRetry;
    }

    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }

    public void addParams(String key, String value) {
        mParams.put(key, value);
    }

    public void addParams(Map<String,String> params) {
        mParams.putAll(params);
    }

    public void addParams(String key, Object value) {
        // 基本数据类型 form-data
        if (isBaseDataType(value)) {
            mParams.put(key, value + "");
        } else {
            // 不是基本类型 multipart/form-data
            mTypeParams.add(new ContentTypeParams(key, value, HttpConfig.OCTET_STREAM, this));
        }
    }

    public void addParams(String key, Object value, String contentType) {
        mTypeParams.add(new ContentTypeParams(key, value, contentType, this));
    }

    // 其他提交  如json，text
    public void otherSubmit(String contentType, String data) {
        // 需要将其他的body参数清空
        mParams.clear();
        mTypeParams.clear();

        // 其他提交参数和类型
        mContentType = contentType;
        mData = data;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    @Override
    public void contentType(String contentType) {
        mContentType = contentType;
    }

    public String getContentType() {
        return mContentType;
    }

    public String getData() {
        return mData;
    }

    public int getMethod() {
        return mMethod;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public Set<ContentTypeParams> getTypeParamses() {
        return mTypeParams;
    }

    public boolean isRetry() {
        return mIsRetry;
    }

    public TimeOuts getTimeOuts() {
        return mTimeOuts;
    }


    // 是否是基本数据类型
    private boolean isBaseDataType(Object object) {
        boolean isBaseDataType = false;
        if (object instanceof String) {
            isBaseDataType = true;
        } else if (object instanceof Integer) {
            isBaseDataType = true;
        } else if (object instanceof Boolean) {
            isBaseDataType = true;
        } else if (object instanceof Float) {
            isBaseDataType = true;
        } else if (object instanceof Long) {
            isBaseDataType = true;
        } else if (object instanceof Double) {
            isBaseDataType = true;
        } else if (object instanceof Character) {
            isBaseDataType = true;
        }
        return isBaseDataType;
    }

}
