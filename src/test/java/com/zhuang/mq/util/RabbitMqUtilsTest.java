package com.zhuang.mq.util;

import com.rabbitmq.client.Channel;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class RabbitMqUtilsTest {

    @Test
    public void send() throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getConnectionFactory().newConnection().createChannel();
        for (int i = 0; i < 1000; i++) {
            RabbitMqUtils.send(channel,"","test01","zwb" +i);
        }
    }

    @Test
    public void receive() throws InterruptedException {
        RabbitMqUtils.receive("test01",c->{
            System.out.println(c);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread.sleep(100000);
    }
}