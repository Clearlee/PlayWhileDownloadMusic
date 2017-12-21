package com.clearlee.playwhiledownloadmusic.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * **
 * 用来打印日志的
 *
 * @author Administrator
 */
public class LogTool {
    static String tag = "mylog";
    /**
     * **
     * 打印一个对象
     *
     * @param
     */
    public static void p(Object s) {
        try {
            Log.e(tag, s.toString());
        } catch (Exception e) {
        } catch (Error error) {
        }
    }

    /**
     * **
     * 控制台打印一个对象
     *
     * @param obj
     */
    public static void s(Object obj) {
        try {
            Log.v(tag, ""+obj);
        }catch (Exception e){}
    }

    /**
     * **
     * 打印一个异常
     *
     * @param
     */
   public static void ex(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        try{
            Log.e(tag, writer.toString());
         }catch (Exception e2){}
    }
}
