package com.zhuang.mq.handler;

public interface ReceiveHandler {
    boolean handle(String message);
}
