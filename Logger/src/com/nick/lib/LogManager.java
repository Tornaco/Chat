package com.nick.lib;

/**
 * Created @2017/5/31 12:12
 */

// 新建了模块，用来做日志处理。
// 换个名字，不然容易和其他同名字的类混淆。
public class LogManager {

    public static void log(String message) {
        System.out.println(message);
    }

    public static void log(Object obj) {
        System.out.println(obj);
    }

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
