package org.dao.support;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import org.dao.IDaoSupport;
import org.dao.annotation.Column;
import org.dao.annotation.Table;
import org.log.L;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/10/23
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class DaoSupport<T> implements IDaoSupport<T> {

    // 数据库操作持有对象
    private SQLiteDatabase mSQLiteDatabase;
    // 存储实体类Class
    private Class<T> mClazz;
    // ContentValues 的所有put函数 避免重复反射
    private Map<String, Method> mPutMethods;

    /**
     * 查询
     */
    private QuerySupport<T> mQuerySupport;

    private String TAG = "DaoSupport";

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {

        this.mSQLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;
        this.mPutMethods = new HashMap<>();
        // 拼接数据库创建语句
        StringBuffer sql = new StringBuffer();

        // 表名注解
        Table tab = mClazz.getAnnotation(Table.class);

        //如果没有注解则异常提示开发者
        if (tab == null) {
            throw new IllegalStateException(clazz.getName() + " 没有在类上声明注解@Table ，请在类上申明注解@Table。");
        }

        // 创建表语句
        sql.append("create table if not exists ") // "IF NOT EXISTS"从句
                // 如果当前创建的数据表名已经存在，即与已经存在的表名、视图名和索引名冲突，
                // 那么本次创建操作将失败并报错。然而如果在创建表时加上"IF NOT EXISTS"从句，
                // 那么本次创建操作将不会有任何影响，即不会有错误抛出，除非当前的表名和某一索引名冲突。
                .append(DaoUtil.getTabName(mClazz))// 表名使用类名
                .append("(id integer primary key autoincrement, ");

        // 遍历所的测成员
        for (Field field : mClazz.getDeclaredFields()) {
            // 设置可操作私有
            field.setAccessible(true);
            // 拿成员上面的注解
            Column column = field.getAnnotation(Column.class);

            // 为空不做处理
            if (column == null) {
                continue;
            }

            // 不为空，建表
            // 列名
            String name = DaoUtil.getColumnName(field);
            // 拿到属性类型 int String boolean...
            String type = field.getType().getSimpleName();
            // 添加时需要类型转换 String --> text int -->Intenger 工具包内转换
            type = DaoUtil.getColumnType(type);

            sql.append(name).append(type).append(", ");
        }

        // 添加完以后需要将最后的"， "  替换掉为")"
        sql.replace(sql.length() - 2, sql.length(), ")");
        // 拼接后的sql语句
        String createTableSql = sql.toString();

        if (L.isDebug()){
            L.e(TAG, "建表语句 -> " + createTableSql);
        }

        // 创建表
        mSQLiteDatabase.execSQL(createTableSql);
    }

    @Override
    public long insert(T data) {
        ContentValues contentValues = contentValuesByObj(data);
        return mSQLiteDatabase.insert(DaoUtil.getTabName(mClazz), null, contentValues);
    }

    @Override
    public void insert(List<T> datas) {
        // 批量插入采用事物
        mSQLiteDatabase.beginTransaction();

        for (T data : datas) {
            // 单条插入
            insert(data);
        }

        // 关闭事物操作
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public int delete(String whereClause, String... whereArgs) {
        return mSQLiteDatabase.delete(DaoUtil.getTabName(mClazz), whereClause, whereArgs);
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSQLiteDatabase.update(DaoUtil.getTabName(mClazz), values, whereClause, whereArgs);
    }

    @Override
    public QuerySupport<T> querySupport() {
        if (mQuerySupport == null) {
            mQuerySupport = new QuerySupport<>(mSQLiteDatabase, mClazz);
        }
        return mQuerySupport;
    }


    // obj 转成 ContentValues
    private ContentValues contentValuesByObj(T obj) {

        /*插入方法   ContentValues values = new ContentValues();
                    values.put("name",person.getName());
                    values.put("age",person.getAge());
                    values.put("flag",person.getFlag());
                    db.insert("Person",null,values);*/

        ContentValues values = new ContentValues();
        // 封装values
        Field[] fields = mClazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                // 拿成员上面的注解
                Column column = field.getAnnotation(Column.class);

                // 为空不做处理
                if (column == null) {
                    continue;
                }

                // 键名称
                String key = DaoUtil.getColumnName(field);
                // 获取value（对应值）
                Object value = field.get(obj);

                // 拿到key值的名称（class.getName）
                String filedTypeName = field.getType().getName();

                //提高性能 避免重复反射，现在集合里面找method 如果没有然后根据反射去找method
                Method putMethod = mPutMethods.get(filedTypeName);

                if (putMethod == null) {
                    // 找到 ContentValues 中 put函数的重载函数
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    //  添加到集合，下次使用直接寻找
                    mPutMethods.put(filedTypeName, putMethod);
                }

                // 通过反射执行
                putMethod.invoke(values, key, value);// ContentValues.put();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return values;
    }
}
