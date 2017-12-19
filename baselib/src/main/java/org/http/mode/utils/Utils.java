package org.http.mode.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.log.L;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/11/21
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class Utils {

    /**
     * 获取接口泛型的实体类型
     */
    public static Class<?> analysisInterfaceInfo(Object object, String callbakName) {
        // 获取接口定义相关泛型
        Type[] genType = object.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = null;//(ParameterizedType) genType[0];
        // 找到对应的interface
        for (int i = 0; i < genType.length; i++) {
            try {
                parameterizedType = (ParameterizedType) genType[i];
                // 类名相匹配
                if (parameterizedType.toString().contains(callbakName)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            // 普通泛型类型
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        } catch (Exception e) {
            // list类型泛型
            ParameterizedType type = (ParameterizedType) parameterizedType.getActualTypeArguments()[0];
            // list对象中的泛型 List<String> -> clazz.getName() =  String
            Class clazz = (Class<?>) type.getActualTypeArguments()[0];
            // 拿list (Class<?>) type.getRawType()  -> (Class<?>) type.getRawType().getName() = List
            return clazz;
//            return (Class<?>) type.getRawType();
        }
    }


    public static <T> T getInstanceByJson(Class<T> clazz, String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) throws Exception {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }


    public static <T> T gson(String json, Class<T> clazz) {

        T result = null;

        try {
            // 解析成List看是否异常
            result = (T) jsonToArrayList(json, clazz);
        } catch (Exception e) {
            // 调试模式下打印堆栈信息
            if (L.isDebug()) {
                e.printStackTrace();
            }

            try {
                // 异常之后表示不能解析成List，name解析成普通对象
                result = jsonToObject(json, clazz);
            } catch (Exception e1) {
                // 调试模式下打印堆栈信息
                if (L.isDebug()) {
                    e1.printStackTrace();
                }
            }

            // 如果解析成普通对象失败，那么判断是否是String类型，也就是直接返回数据结果
            if (result == null && clazz == String.class) {
                return (T) json;
            }
        }
        return result;
    }
}
