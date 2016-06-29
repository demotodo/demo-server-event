package com.demotodo.demo.servlet;

import com.demotodo.demo.utils.HttpUtils;
import com.demotodo.demo.utils.ThreadUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bribin.zheng on 2016/6/27.
 */
@WebServlet(name = "asyncServlet", urlPatterns = "/asyncServlet", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    //    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private final ExecutorService executorService = Executors.newFixedThreadPool(300);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setHeader("Connection", "Keep-Alive");
        resp.setContentType("text/html;charset=utf-8");
        HttpUtils.addNonCacheOptions(resp);

        PrintWriter out = resp.getWriter();
        out.write("hello");
        out.write("<br/>");

        final AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(300 * 1000L);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // to demo some heavy task
                ThreadUtils.sleep(2000);

                try {
                    PrintWriter out = asyncContext.getResponse().getWriter();
                    out.write("world! (from asyc context)");
                    asyncContext.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }

}
