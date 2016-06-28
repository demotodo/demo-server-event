package com.demotodo.servlet3;

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
@WebServlet(name = "sourceServlet", urlPatterns = "/sourceServlet")
public class SourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream; charset=utf-8");

        PrintWriter out = resp.getWriter();

        for (int i = 0; i < 300; i++) {
            out.println("data: normal data");
            out.println();
            out.println("event: ping");
            out.println("data: {");
            out.println("data: \"time\": " + System.currentTimeMillis());
            out.println("data: }");
            out.println("id: ID-" + i);
            out.println("retry: 10000");
            out.println();
            out.flush();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
