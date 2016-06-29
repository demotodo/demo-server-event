package com.demotodo.demo.servlet;

import com.demotodo.demo.utils.HttpUtils;
import com.demotodo.demo.utils.ThreadUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by bribin.zheng on 2016/6/28.
 */
@WebServlet(name = "blockServlet", urlPatterns = "/blockServlet")
public class BlockServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        HttpUtils.addNonCacheOptions(resp);

        // to demo some heavy task
        ThreadUtils.sleep(2000);

        PrintWriter out = resp.getWriter();
        out.write("<html>\n"
            + "<body>\n"
            + "<h2>Hello World!</h2>\n"
            + "</body>\n"
            + "</html>\n");
        out.flush();
    }

}
