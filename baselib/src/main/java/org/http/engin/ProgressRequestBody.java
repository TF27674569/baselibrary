package org.http.engin;

import org.http.mode.callback.BaseCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * description： 重写body实现上传进度
 * <p/>
 * Created by TIAN FENG on 2017/10/19
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

class ProgressRequestBody extends RequestBody {

    private RequestBody mRequestBody;

    //进度回调接口
    private BaseCallback mCallBack;
    //包装完成的BufferedSink
    private BufferedSink mBufferedSink;

    public ProgressRequestBody(RequestBody requestBody, BaseCallback callback) {
        this.mRequestBody = requestBody;
        this.mCallBack = callback;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            //包装
            mBufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        mRequestBody.writeTo(mBufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        mBufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {

        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if (mCallBack != null) {
                    mCallBack.onProgress(contentLength, bytesWritten);
                }
            }
        };
    }
}
