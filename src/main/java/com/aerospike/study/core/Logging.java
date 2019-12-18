package com.aerospike.study.core;

import com.aerospike.client.Log;

import java.util.Date;

/**
 * @ClassName Logging
 * @Deseription TODO
 * @Author lxy_m
 * @Date 2019/12/18 14:33
 * @Version 1.0
 * Aerospike Java客户端包含一个日志接口，可用于调试。
 */
public class Logging implements Log.Callback {

    //默认情况下,禁用日志记录
    /**
     * 要启用日志记录，请提供回调以为每个日志消息调用。
     * 确保回调是的实例com.aerospike.client.Log.Callback
     *  public interface Callback {
     *     public void log(Log.Level level, String message);
     *  }
     *  要启用日志记录，请使用创建一个实例Callback并将其设置为日志记录回调Log.setCallback()：
     *  class Log {
     *         public static void setCallback(Log.Callback callback);
     *     }
     *
     */

    @Override
    public void log(Log.Level level, String message) {
        Date date = new Date();
        System.out.println(date.toString() + " " + level + " " + message);
    }

    /**
     * 日志级别
     * 要控制发送到日志回调的日志的详细程度，请使用Log.setLevel()。
     * class Log {
     *         public static void setLevel(Log.Level level);
     *     }
     *  日志级别在中定义com.aerospike.client.Log.Level
     *  要记录所有调试消息，请将级别设置为Log.Level.DEBUG：
     *  在其他程序中使用测试
     * @param args
     */
    public static void main(String[] args) {
        Log.Callback myLog = new Logging();
        Log.setCallback(myLog);
        Log.setLevel(Log.Level.DEBUG);
    }
}
