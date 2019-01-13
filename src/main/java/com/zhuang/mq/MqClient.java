package com.zhuang.mq;

public interface MqClient {

    public void connect();

    public void send(String message);

    public void close();

}
