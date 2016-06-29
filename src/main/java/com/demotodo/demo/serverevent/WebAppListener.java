package com.demotodo.demo.serverevent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
@WebListener
public class WebAppListener implements HttpSessionListener, ServletContextListener {

    private MessageService messageService = MessageService.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("WebAppListener.sessionDestroyed, session id: " + se.getSession().getId());
        messageService.unregister(se.getSession().getId());
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WebAppListener.contextDestroyed");
        messageService.shutdown();
    }

}
