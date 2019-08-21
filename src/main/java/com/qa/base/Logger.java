//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.qa.base;


import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Logger {
    public Logger() {
    }

    public static void error(String message) {
        info("Error " + message);
    }

    public static void step(String message) {
        info(message);
    }

    public static void info(String message) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        System.out.println(MessageFormat.format("({0}): {1}", df.format(new Date()), message));
    }

    public static void info(String expectedValue, String actualValue, String message) {
        String output = MessageFormat.format("验证成功 | 预期结果:{0} | 实际结果:{1} | {2} ", expectedValue, actualValue, message);
        output(output);
    }

    public static void error(String expectedValue, String actualValue, String message) {
        String output = MessageFormat.format("验证失败 | 预期结果:{0} | 实际结果:{1} | {2} ", expectedValue, actualValue, message);
        output(output);
    }

    public static void output(String message) {
        if (!message.trim().equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String output = MessageFormat.format("({0}): {1}", df.format(new Date()), message);
            System.out.println(message);
        }
    }




}
