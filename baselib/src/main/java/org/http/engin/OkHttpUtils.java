package org.http.engin;

import android.text.TextUtils;

import org.http.mode.callback.BaseCallback;
import org.http.mode.params.ContentTypeParams;
import org.http.mode.params.HttpConfig;
import org.http.mode.params.HttpParams;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

class OkHttpUtils {
    /**
     * 拼接参数
     */
    static String jointParams(HttpParams params) {

        if (params.getParams().size() <= 0 && params.getTypeParamses().size() <= 0) {
            return params.getUrl();
        }

        StringBuffer stringBuffer = new StringBuffer(params.getUrl());
        if (!params.getUrl().contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!params.getUrl().endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        // 多参数合并
        for (ContentTypeParams contentTypeParams : params.getTypeParamses()) {
            if (contentTypeParams.value instanceof File) {
                throw new IllegalArgumentException("get request params mast be string . your key " + contentTypeParams.key + " value type is file.");
            } else if (contentTypeParams.value instanceof List) {
                throw new IllegalArgumentException("get request params mast be string . your key " + contentTypeParams.key + " value type is List.");
            }
        }

        params.getTypeParamses().clear();

        for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }


        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    static void addHeader(Request.Builder builder, HttpParams params) {
        addHeader(builder, params, 0);
    }

    /**
     * @param fileSize 已经此在了的大小
     */
    static void addHeader(Request.Builder builder, HttpParams params, long fileSize) {
        // 需要判断是否是下载
        if (params.getMethod() == HttpConfig.DOWN_LOAD) {
            // 需要判断下载是否需要断点续传
            if (params.isAutoResume()) {
                // 添加断点续传的标记
                builder.header("RANGE", "bytes=" + fileSize + "-");
            }
        }

        for (Map.Entry<String, String> entry : params.getHeader().entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
        builder.tag(params.getTag() == null ? params.getUrl() : params.getTag());
    }

    static void addBody(Request.Builder builder, HttpParams params, BaseCallback callBack) {
        // 取消标记
        builder.tag(params.getTag() == null ? params.getUrl() : params.getTag());

        // 表单提交没有body参数
        if (params.getParams().size() == 0 && params.getTypeParamses().size() == 0) {
            if (TextUtils.isEmpty(params.getData())) {
                // post请求无参数
                builder.post(new ProgressRequestBody(RequestBody.create(null, ""), callBack));
            } else {
                RequestBody body = RequestBody.create(MediaType.parse(params.getContentType()), params.getData());
                // 提交json，提交text
                builder.post(new ProgressRequestBody(body, callBack));
            }
            return;
        }

        // 使用from-data上传
        if (params.getContentType().equals(HttpConfig.FROM_DATA)) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            builder.post(new ProgressRequestBody(formBuilder.build(), callBack));
            return;
        }

        // 使用 multipart/form-data 上传
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        // 设置type
        multipartBuilder.setType(MultipartBody.FORM);

        // 添加请求参数
        for (Map.Entry<String, String> entry : params.getParams().entrySet()) {
            multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        // 添加文件
        for (ContentTypeParams contentTypeParams : params.getTypeParamses()) {
            // 获取value
            Object value = contentTypeParams.value;
            // 如果value是文件
            if (value instanceof File) {
                // value文件
                File valueFile = (File) value;
                // type 类型
                String contentType = getContentType(contentTypeParams);
                // 请求头
                RequestBody body = RequestBody.create(MediaType.parse(contentType), valueFile);
                // 添加请求参数为文件
                multipartBuilder.addFormDataPart(contentTypeParams.key, valueFile.getName(), body);
            } else {
                multipartBuilder.addFormDataPart(contentTypeParams.key, value + "");
            }
        }
        builder.post(new ProgressRequestBody(multipartBuilder.build(), callBack));
    }

    private static String getContentType(ContentTypeParams contentTypeParams) {

        // 判断有没有定义contentType
        if (!TextUtils.isEmpty(contentTypeParams.contentType)) {
            // 定义后直接返回
            return contentTypeParams.contentType;
        }
        // 通过URLConnection获取ContentType的类型
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        // 获取此文件的contengType
        String contentTypeFor = fileNameMap.getContentTypeFor(((File) contentTypeParams.value).getPath());
        // 如果没有获取到则使用二进制的流上传
        if (TextUtils.isEmpty(contentTypeFor)) {
            contentTypeFor = HttpConfig.OCTET_STREAM;
        }
        return contentTypeFor;
    }

    static String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    public static void callError(BaseCallback callback, String url, Exception e) {
        callback.onError(url, e);
        callback.onFinal(url);
    }
}
