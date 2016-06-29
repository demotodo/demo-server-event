package com.demotodo.demo.serverevent;

import com.demotodo.demo.utils.HttpUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
@WebServlet(name = "messageServlet", urlPatterns = "/messageServlet", asyncSupported = true)
public class MessageServlet extends HttpServlet {

    private MessageService messageService = MessageService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream; charset=utf-8");
        HttpUtils.addNonCacheOptions(resp);

        AsyncContext asyncContext = req.startAsync();
        System.out.println("MessageServlet.doGet");

        messageService.register(asyncContext);
    }

}
