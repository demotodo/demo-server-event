package com.demotodo.demo.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
public class HttpUtils {

    public static void addNonCacheOptions(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        response.setHeader("Pragma", "no-cache"); //HTTP 1.0 controls
        response.setDateHeader("Expires", 0);
    }

}
