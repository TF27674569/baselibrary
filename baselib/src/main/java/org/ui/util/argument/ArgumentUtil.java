package org.ui.util.argument;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数传递工具
 * 
 * @author TIAN FENG
 *
 */
public class ArgumentUtil {

	private static volatile ArgumentUtil mInstance;
	private List<Object> mTargets;

	private ArgumentUtil() {
		mTargets = new ArrayList<>();
	}

	public static ArgumentUtil get() {
		if (mInstance == null) {
			synchronized (ArgumentUtil.class) {
				if (mInstance == null) {
					mInstance = new ArgumentUtil();
				}
			}
		}
		return mInstance;
	}

	/**
	 * 注册
	 * 
	 * @param target
	 */
	public void register(Object target) {
		mTargets.add(target);
	}

	/**
	 * 解除注册
	 * 
	 * @param target
	 */
	public void unRegister(Object target) {
		mTargets.remove(target);
	}

	/**
	 * 发送参数或事件
	 */
	public void post(Object... objects) {
		// 遍历注册的对象
		for (Object object : mTargets) {
			// class -> java
			Class<?> clazz = object.getClass();
			// 获取所有的方法
			Method[] methods = clazz.getDeclaredMethods();
			// 遍历方法
			for (Method method : methods) {
				// 可操作私有
				method.setAccessible(true);
				// 拿到标识
				Subscribe subscribe = method.getAnnotation(Subscribe.class);
				// 存在标识
				if (subscribe != null) {
					try {
						// 调用方法
						method.invoke(object, objects);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
