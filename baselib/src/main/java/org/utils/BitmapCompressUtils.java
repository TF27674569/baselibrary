package org.utils;

import android.graphics.Bitmap;

import jni.HuffmanCompress;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/20
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class BitmapCompressUtils {

    /**
     *  压缩图片，通过huffman
     * @param bitmap  图片
     * @param quality  质量一般取18 - 28之间的值
     * @param path 压缩后的路径
     */
    public static void compress(Bitmap bitmap,int quality,String path){
        HuffmanCompress.compress(bitmap, quality, path);
    }

    /**
     *  压缩图片，通过huffman
     * @param path  图片文件
     * @param quality  质量一般取18 - 28之间的值
     * @param path 压缩后的路径
     */
    public static void compress(String path, int quality){
        HuffmanCompress.compress(HuffmanCompress.decodeFile(path), quality, path);
    }

}
