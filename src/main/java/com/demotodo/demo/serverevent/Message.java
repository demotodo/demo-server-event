package com.demotodo.demo.serverevent;

/**
 * Created by bribin.zheng on 2016/6/29.
 */
public class Message {

    private String type;
    private long time;
    private String msg;

    private Message(String type, long time, String msg) {
        this.type = type;
        this.time = time;
        this.msg = msg;
    }

    public static Message createNormalMessage(String msg) {
        return new Message("msg", System.currentTimeMillis(), msg);
    }

    public static Message createCloseMessage(String msg) {
        return new Message("close", System.currentTimeMillis(), msg);
    }

    public boolean isLast() {
        return "close".equals(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
