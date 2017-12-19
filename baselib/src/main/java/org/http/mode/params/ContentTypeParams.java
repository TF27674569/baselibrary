package org.http.mode.params;

import java.io.File;
import java.util.List;

/**
 * description：带type上传
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class ContentTypeParams {

    // 键
    public String key;
    // 值
    public Object value;
    // type
    public String contentType;


    public ContentTypeParams(String key, Object value, String contentType, MultipartType type) {
        this.key = key;
        this.value = value;
        this.contentType = contentType;
        checkType(type);
    }

    private void checkType(MultipartType type) {
        // 如果有文件必须使用分段上传
        if (value instanceof File || value instanceof List) {
            type.contentType(HttpConfig.MULTIPART_FROM_DATA);
        }
    }

    @Override
    public String toString() {
        String val = value.toString();
        if (value instanceof File) {
            val = ((File) value).getPath();
        }
        return "{ " + key + " = " + val + "  contentType: " + contentType + " }\n      ";
    }

    interface MultipartType {
        void contentType(String contentType);
    }
}
