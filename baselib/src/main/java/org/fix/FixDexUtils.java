package org.fix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;

import dalvik.system.BaseDexClassLoader;

/**
 * Description :
 * Created : TIAN FENG
 * Date : 2017/3/29
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class FixDexUtils {

    /**
     *  从classLoader中获取dexElements
     */
    public static Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {
        /* 思路：
         * 1. 先从BaseDexClassLoader中获取 pathList 的属性
         * 2. 然后从 pathList 中获取 dexElements 的属性
         */

        // 1.1. 从BaseDexClassLoader中获取 pathList 的属性
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        // 1.2. 设置可以操作私有
        pathListField.setAccessible(true);
        // 1.3. 从 classLoader中反射拿到pathList的值
        Object pathList = pathListField.get(classLoader);

        // 2.1 然后从 pathList 中获取 dexElements 的属性
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        // 2.2 设置可操作私有
        dexElementsField.setAccessible(true);
        // 2.3 从 pathList 获取 dexElements 的值
        return dexElementsField.get(pathList);
    }

    /**
     * 反射方式合并两个数组
     *
     * @param arrayLhs  插入在前面的数组
     * @param arrayRhs  插入在后面的数组
     * @return
     */
    public static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    /**
     * 将dex文件拷贝到odex文件目录下
     *
     * @param src  目标文件
     * @param dest odex目录下文件
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            // 判断odex目录下是否存在dest文件
            if (!dest.exists()) {
                // 不存在创建一个
                dest.createNewFile();
            }
            // 获取目标文件的流输入流
            inChannel = new FileInputStream(src).getChannel();
            // 获取dest文件的输出流
            outChannel = new FileOutputStream(dest).getChannel();
            // 拷贝
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}
