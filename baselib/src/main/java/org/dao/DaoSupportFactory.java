package org.dao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import org.dao.support.DaoSupport;

import java.io.File;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/10/23
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class DaoSupportFactory {

    // 数据库名
    private static String DB_NAME = "dao_name.db";
    private SQLiteDatabase mSqLiteDatabase;
    private static volatile DaoSupportFactory mDaoFactory;

    // 初始化赋值数据库名
    public static void initDao(String dbName){
        if (dbName.endsWith(".db")){
            DB_NAME = dbName;
        }else {
            DB_NAME = dbName+".db";
        }
    }

    // 获取实例
    public static DaoSupportFactory get(){
        if(mDaoFactory==null){
            synchronized (DaoSupportFactory.class){
                if (mDaoFactory==null){
                    mDaoFactory = new DaoSupportFactory();
                }
            }
        }
        return mDaoFactory;
    }

    // 获取操作对象
    public <T> IDaoSupport<T> getDao(Class<T> clazz){
        IDaoSupport<T> daoSoupport = new DaoSupport<T>();
        daoSoupport.init(mSqLiteDatabase,clazz);
        return daoSoupport;
    }

    private DaoSupportFactory(){
        // 拼接根目录
        StringBuffer rootPath = new StringBuffer();
        rootPath.append(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(File.separator)
                .append("dao")
                .append(File.separator)
                .append("database");

        // 把数据库存到内存卡中，调用时需要判断版本是否需要动态申请权限
        File dbRoot = new File(rootPath.toString());

        // 如果不存在则创建一个
        if (!dbRoot.exists()){
            dbRoot.mkdirs();
        }
        // 创建数据库文件
        File dbFile = new File(dbRoot, DB_NAME);

        // 创建或打开一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile,null);

    }

}
