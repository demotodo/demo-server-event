package com.demotodo.demo.serverevent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
public class SenderThread extends Thread {

    private MessageService messageService;
    private boolean stop;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public SenderThread(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void run() {
        while (!stop) {
            synchronized (messageService) {
                long count = messageService.messagePublished();

                System.out.println(sdf.format(new Date()) + " SenderThread.run, call sendMessages()");
                try {
                    messageService.sendMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(sdf.format(new Date()) + " count = " + count);
                System.out.println(sdf.format(new Date()) + " published = " + messageService.messagePublished());
                if (count == messageService.messagePublished()) {
                    try {
                        System.out.println(sdf.format(new Date()) + " SenderThread.run, waiting messages ...");
                        // will be notified, or wait to timeout
                        messageService.wait(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(sdf.format(new Date()) + " SenderThread.run, call sendMessages() again due to new messages arrival");
                }
            }
        }
    }

    public void halt() {
        System.out.println("SenderThread.halt");
        this.stop = true;

        synchronized (messageService) {
            messageService.notify();
        }
    }

}
