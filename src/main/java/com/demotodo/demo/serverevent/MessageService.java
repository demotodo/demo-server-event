package com.demotodo.demo.serverevent;

import com.google.gson.Gson;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
public class MessageService {

    private static final Map<String, AsyncContext> sessions = new ConcurrentHashMap<>();
    private static final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    // can move to Redis, and set timeout on messasge
    private static final Map<String, List<Message>> messages = new ConcurrentHashMap<>();

    private static final Map<String, Long> maxMessageIds = new ConcurrentHashMap<>();
    private static final Map<String, Long> lastMessageIds = new ConcurrentHashMap<>();

    private final AtomicLong messagePublished = new AtomicLong(0);

    private final SenderThread sender;

    private static MessageService instance;

    public static synchronized MessageService getInstance() {
        if (instance == null)
            instance = new MessageService();
        return instance;
    }

    public MessageService() {
        sender = new SenderThread(this);
        sender.start();
    }

    public synchronized void register(final AsyncContext asyncContext) {
        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();

        asyncContext.setTimeout(300 * 1000);
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("MessageService.onComplete " + asyncContext);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                System.out.println("MessageService.onTimeout " + asyncContext);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                System.out.println("MessageService.onError " + asyncContext);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                System.out.println("MessageService.onStartAsync " + asyncContext);
            }
        });

        if (!sessions.containsKey(sessionId)) {
            System.out.println("MessageService.register, new session, async context: " + asyncContext);
            sessions.put(sessionId, asyncContext);
            locks.put(sessionId, new ReentrantLock());

            List<Message> messageList = new Vector<>();
            messages.put(sessionId, messageList);

            maxMessageIds.put(sessionId, 0L);
            lastMessageIds.put(sessionId, 0L);

            // demo to generate processing messages
            new RandomProducer(this, sessionId).start();

        } else {
            ReentrantLock lock = locks.get(sessionId);
            try {
                lock.lock();

                System.out.println("MessageService.register, existing session id: " + sessionId);
                AsyncContext existingAsyncContext = sessions.put(sessionId, asyncContext);
                if (!asyncContext.equals(existingAsyncContext)) {
                    existingAsyncContext.complete();
                    System.out.println("MessageService.register, close previous async context: " + existingAsyncContext);
                }

                Long t = Long.valueOf(asyncContext.getRequest().getParameter("t"));
                System.out.println("MessageService.register, reset lastMsgId: " + t);
                lastMessageIds.put(sessionId, t);
            } finally {
                lock.unlock();
            }
        }
    }

    public void unregister(String sessionId) {
        sessions.remove(sessionId);
        locks.remove(sessionId);

        messages.remove(sessionId);
        maxMessageIds.remove(sessionId);
        lastMessageIds.remove(sessionId);
    }

    public void shutdown() {
        sender.halt();
    }

    public void publishMessage(String sessionId, Message message) {
        if (messages.containsKey(sessionId)) {
            System.out.println("MessageService.publishMessage, session id: " + sessionId + ", message: " + new Gson().toJson(message));
            messages.get(sessionId).add(message);
            maxMessageIds.put(sessionId, message.getTime());
            messagePublished.incrementAndGet();

            synchronized (this) {
                this.notify();
            }
        }
    }

    public long messagePublished() {
        return messagePublished.get();
    }

    public void sendMessages() {
        System.out.println("MessageService.sendMessages, start to send messages ...");
        int count = 0;

        for (Map.Entry<String, List<Message>> queueEntry : messages.entrySet()) {
            String sessionId = queueEntry.getKey();
            ReentrantLock lock = locks.get(sessionId);
            try {
                lock.lock();

                Long lastMsgId = lastMessageIds.get(sessionId);
                if (lastMsgId >= maxMessageIds.get(sessionId))
                    continue;

                List<Message> messageList = queueEntry.getValue();
                for (int i = 0; i < messageList.size(); i++) {
                    Message message = messageList.get(i);
                    if (message.getTime() <= lastMsgId)
                        continue; // already sent, can be removed, or expired

                    sendMessage(sessionId, message);
                    lastMessageIds.put(sessionId, message.getTime());
                    count++;

                    if (message.isLast()) {
                        unregister(sessionId);
                        break;
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        System.out.println("MessageService.sendMessages, sent " + count + " messages");
    }

    private void sendMessage(String sessionId, Message message) {
        sendMessage(sessions.get(sessionId), message);
    }

    private void sendMessage(AsyncContext asyncContext, Message message) {
        try {
            String msg = "event: " + message.getType() + "\n"
                + "data: " + new Gson().toJson(message) + "\n";
            System.out.println("MessageService.sendMessage, message: \n" + msg);

            //TODO using following codes can't detect client disconnect
//            PrintWriter PrintWriterout = asyncContext.getResponse().getWriter();
//            out.println(msg);
//            out.flush();

            ServletOutputStream outputStream = asyncContext.getResponse().getOutputStream();
            outputStream.println(msg);
            outputStream.println();
            outputStream.flush();

            for (int i = 0; i < 10; i++) {
                outputStream.println();
                outputStream.flush();
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
