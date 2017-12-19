package org.fix;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Description : 热修复管理类
 * Created : TIAN FENG
 * Date : 2017/3/29
 * Email : 27674569@qq.com
 * Version : 1.0
 */
public class FixDexManager {

    /*思路：
     *
     * dex包加载时的路径在app路径下的odex文件夹下
     *
     * 1.在app运行的时候我们需要判断本地是否有dex包
     * 2.如果本地存在dex包则需要将dex包下面的dexElements插入的app的dexElements的前面
     * 3.在源码中类的加载是通过classLoader的子类BaseDexClassLoader调用loadclass方法通过类名反射newInstance创建
     *   在BaseDexClassLoader中findclass方法委托给了pathList（实体类DexPathList）的findClass方法，
     *   而在pathList的findClass方法中通过for循环
     *   for(Element element:dexElements){
     *           ...
     *      if（clzz！= null）{
     *          return clazz; // 拿到这个类后就会停止遍历（相同的类名,只会返回前面一个）
     *      }
     *   }
     * 源码得知 在DexPathList类中有dexElements 通过此属性和类名能返回clazz对象 最后通过反射创建类
     *
     * 4.既然dexElements是一个数组 我们就将dex包的dexElements插入到运行app的dexElements及可
     * 5. 本类代码从 fixDexFiles（）方法开始阅读
     */

    private Context mContext;
    // dex文件夹
    private File mDexDir;

    public FixDexManager(Context context) {
        this.mContext = context;
        // 获取应用可以访问的odex目录
        mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复应用
     *
     * @param fixDexPath dex包路径
     */
    public void fixDex(String fixDexPath) throws Exception {
        // 获取下载好的补丁的dexElement对象
        // 1. 移动到odex目录下 拿到这个file-->dex
        File srcFile = new File(fixDexPath);
        // 2. 判断所给路径是否存在
        if (!srcFile.exists()) {
            //  不存在直接异常
            throw new FileNotFoundException(fixDexPath);
        }
        // 3. 在odex目录下创建这个文件
        File desFile = new File(mDexDir, srcFile.getName());
        // 4. 判断是否已经存在了此名字的加载包
        if (desFile.exists()) {
            // 如果存在 直接加载
            loadFixDex();
            return;
        }
        // 5. copy srcFile 到 desFile
        FixDexUtils.copyFile(srcFile, desFile);
        // 6. 修复
        List<File> fixlist = new ArrayList<File>();
        fixlist.add(desFile);
        // 7.开始加载
        fixDexFiles(fixlist);
    }

    /**
     * 加载全部的修复包
     */
    public void loadFixDex() throws Exception {
        // 拿到odex文件夹下的所有文件
        File[] dexFiles = mDexDir.listFiles();
        // 修复集合
        List<File> fixDexFiles = new ArrayList<>();
        // 将文件加入到集合
        for (File dexFile : dexFiles) {
            // 加载.dex后戳名文件
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        // 加载全部
        fixDexFiles(fixDexFiles);
    }

    /****************************************************************************************************************/
    /**
     * 修复所有的 dex包
     *
     * @param fixDexFiles dex包的路径下的所有文件
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception {
        // 1. 获取应用的classLoader
        ClassLoader appClassLoader = mContext.getClassLoader();
        // 2. 获取应用的dexElements;
        Object appElements = FixDexUtils.getDexElementsByClassLoader(appClassLoader);
        // 3. 拿到odex解压路径
        File optimizedDirectory = new File(mDexDir,"odex");
        // 4. 判断是否存在
        if (!optimizedDirectory.exists()){
            // 不存在则创建一个
            optimizedDirectory.mkdir();
        }
        // 5. 拿fixDexFiles的每一个dex的classLoader
        for (File fixDexFile:fixDexFiles){
            // 5.1 创建classLoader
            /*需要的参数：
             * 1. dexPath               dex的路径
             * 2. optimizedDirectory    解压路径
             * 3. libraryPath           .so路径
             * 4. parent                父ClassLoader
             */
            ClassLoader fixDexFileClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),
                    optimizedDirectory,
                    null,
                    appClassLoader);
            // 5.2 拿到fix的Elements
            Object fixDexElements = FixDexUtils.getDexElementsByClassLoader(fixDexFileClassLoader);
            // 5.3 合并为一个DexElements 且需要将fixdex插入在前面
            appElements = FixDexUtils.combineArray(fixDexElements,appElements);
        }
        // 6.新的appElements赋值到原来的classLoader中
        injectDexElements(appClassLoader,appElements);
    }

    /**
     * 将修复的 DexElement 赋值到 appClassLoader中
     *
     * @param appClassLoader    应用的classLoader
     * @param appElements       修复好的DexElement
     */
    private void injectDexElements(ClassLoader appClassLoader, Object appElements) throws Exception {
        // 1. 获取BaseDexClassLoader中的 pathList 属性
        Field appPathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        // 2. 设置可以操作私有
        appPathListField.setAccessible(true);
        // 3. 拿到 appClassLoader 中 pathList 属性的值
        Object appPathList = appPathListField.get(appClassLoader);
        // 4. 拿到 appPathList 中的 dexElements 属性
        Field appDexElementsField = appPathList.getClass().getDeclaredField("dexElements");
        // 5. 设置可以操作私有
        appDexElementsField.setAccessible(true);
        // 6. 反射赋值
        appDexElementsField.set(appPathList,appElements);
    }

}
