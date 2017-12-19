package org.utils;

import android.content.Context;

import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * description： SharedPreferences工具封装
 * <p/>
 * Created by TIAN FENG on 2017年9月4日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class PreferencesUtil {
	private static SharedPreferences sharedPreferences;
	private static SharedPreferences.Editor editor;

	private PreferencesUtil() {
	}

	/**
	 * 初始化 建议在appliocation中初始化
	 *
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件名
	 */
	public static void init(Context context, String fileName) {
		sharedPreferences = context.getApplicationContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	/**
	 * 保存数据的方法，拿到数据保存数据的基本类型，然后根据类型调用不同的保存方法
	 */
	public static void put(String key, Object object) {
		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			// 表示不是基本类型的对象base64转码储存
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				// 创建对象输出流，并封装字节流
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				// 将对象写入字节流
				oos.writeObject(object);
				// 将字节流编码成base64的字符
				String encodedString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
				// 存储String对象
				editor.putString(key, encodedString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		editor.apply();
	}

	/**
	 * 获取保存数据的方法，我们根据默认值的到保存的数据的具体类型，然后调用相对于的方法获取值 如果 defaultObject 与返回类型不匹配
	 * 如值是int 返回为 boolean 则会抛出类型强制转换异常
	 *
	 * @param key 键
	 * @param defaultObject 默认值
	 * @return
	 */
	public static <T> T get(String key, Object defaultObject) throws ClassCastException {
		if (defaultObject instanceof String) {
			defaultObject = sharedPreferences.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			defaultObject = sharedPreferences.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			defaultObject = sharedPreferences.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			defaultObject = sharedPreferences.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			defaultObject = sharedPreferences.getLong(key, (Long) defaultObject);
		} else {
			// 表示不是基本类型的对象base64解码获取
			String productBase64 = sharedPreferences.getString(key, "");
			if (!productBase64.equals("")) {
				// 读取字节
				byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);
				// 封装到字节流
				ByteArrayInputStream bais = new ByteArrayInputStream(base64);
				try {
					// 再次封装
					ObjectInputStream bis = new ObjectInputStream(bais);
					// 读取对象
					defaultObject = bis.readObject();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return (T) defaultObject;
	}

	/**
	 * 设置登录状态
	 */
	public static void login(boolean login) {
		put("PreferencesUtil_islogin", login);
	}

	/**
	 * 获取登录状态
	 */
	public static boolean isLogin() {
		return get("PreferencesUtil_islogin", false);
	}

	/**
	 * 移除某个key值已经对应的值
	 *
	 * @param key
	 */
	public static void remove(String key) {
		editor.remove(key);
		editor.apply();
	}

	/**
	 * 清除所有的数据
	 */
	public static void clear() {
		editor.clear();
		editor.apply();
	}

	/**
	 * 查询某个key是否存在
	 */
	public static boolean contains(String key) {
		return sharedPreferences.contains(key);
	}

	/**
	 * 返回所有的键值对
	 */
	public static Map<String, ?> getAll() {
		return sharedPreferences.getAll();
	}
}
