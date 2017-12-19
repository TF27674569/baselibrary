package org.log;


import org.log.mode.AndroidLogAdapter;
import org.log.mode.LoggerPrinter;
import org.log.mode.Printer;

public final class L {

    // 打印工具
    private static Printer printer;
    //调试开关
    private static boolean sIsDebug = false;

    static {
        printer = new LoggerPrinter();
        printer.addAdapter(new AndroidLogAdapter());
    }

    /**
     * 是否调试
     */
    public static void debug(boolean debug) {
        sIsDebug = debug;
    }

    /**
     * 是否调试
     */
    public static boolean isDebug() {
        return sIsDebug;
    }

    private L() {
        throw new IllegalStateException("no instance");
    }


    /**
     * verbose
     */
    public static void v(String message) {
        if (sIsDebug)
            printer.v(message, (Object[]) null);
    }

    public static void v(Object object) {
        if (sIsDebug)
            printer.v(object);
    }

    public static void v(String tag, String message) {
        if (sIsDebug)
            printer.t(tag).v(message, (Object[]) null);
    }

    /**
     * debug
     */
    public static void d(String message) {
        if (sIsDebug)
            printer.d(message, (Object[]) null);
    }

    public static void d(Object object) {
        if (sIsDebug)
            printer.d(object);
    }

    public static void d(String tag, String message) {
        if (sIsDebug)
            printer.t(tag).d(message, (Object[]) null);
    }

    /**
     * info
     */
    public static void i(String message) {
        if (sIsDebug)
            printer.i(message, (Object[]) null);
    }

    public static void i(Object object) {
        if (sIsDebug)
            printer.i(object);
    }

    public static void i(String tag, String message) {
        if (sIsDebug)
            printer.t(tag).i(message, (Object[]) null);
    }


    /**
     * warn
     */
    public static void w(String message) {
        if (sIsDebug)
            printer.w(message, (Object[]) null);
    }

    public static void w(Object object) {
        if (sIsDebug)
            printer.w(object);
    }

    public static void w(String tag, String message) {
        if (sIsDebug)
            printer.t(tag).w(message, (Object[]) null);
    }

    /**
     * error
     */
    public static void e(String message) {
        if (sIsDebug)
            printer.e(null, message, (Object[]) null);
    }

    public static void e(Object object) {
        if (sIsDebug)
            printer.e(object);
    }

    public static void e(String tag, String message) {
        if (sIsDebug)
            printer.t(tag).e(message, (Object[]) null);
    }


    public static void wtf(String message) {
        if (sIsDebug)
            printer.wtf(message, (Object[]) null);
    }


    public static void json(String json) {
        if (sIsDebug)
            printer.json(json);
    }

    public static void json(String tag,String json) {
        if (sIsDebug)
            printer.t(tag).json(json);
    }


    public static void xml(String xml) {
        if (sIsDebug)
            printer.xml(xml);
    }

}
