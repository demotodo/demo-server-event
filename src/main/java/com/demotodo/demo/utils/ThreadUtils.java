package com.demotodo.demo.utils;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
public class ThreadUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
