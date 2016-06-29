package com.demotodo.demo.serverevent;

import com.demotodo.demo.utils.ThreadUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
public class RandomProducer extends Thread {

    private MessageService messageService;
    private String sessionId;

    public RandomProducer(MessageService messageService, String sessionId) {
        this.messageService = messageService;
        this.sessionId = sessionId;
    }

    @Override
    public void run() {
        int loop = randomInt(10, 20);

        String msg = "going to send " + loop + " messages to session id:" + sessionId;
        System.out.println("RandomProducer.run, msg: " + msg);
        messageService.publishMessage(sessionId, Message.createNormalMessage(msg));

        for (int i = 0; i < loop; i++) {
            msg = "rand message #" + i;
            System.out.println("RandomProducer.run, msg: " + msg);
            messageService.publishMessage(sessionId, Message.createNormalMessage(msg));
            ThreadUtils.sleep(randomInt(1, 5) * 1000);
        }

        System.out.println("RandomProducer.run, msg: finish");
        messageService.publishMessage(sessionId, Message.createCloseMessage("finish"));
    }

    public int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

}
