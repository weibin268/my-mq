package com.zhuang.mq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhuang.mq.config.MyMqProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {

    private static volatile ConnectionFactory connectionFactory;

    public static ConnectionFactory getConnectionFactory() {

        if (connectionFactory == null) {
            synchronized (ConnectionFactory.class) {
                if (connectionFactory == null) {
                    connectionFactory = new ConnectionFactory();
                    MyMqProperties myMqProperties = MyMqProperties.getInstance();
                    connectionFactory.setHost(myMqProperties.getRabbitmqHost());
                    connectionFactory.setUsername(myMqProperties.getRabbitmqUsername());
                    connectionFactory.setPassword(myMqProperties.getRabbitmqPassword());
                    if (myMqProperties.getRabbitmqPort() != null && !myMqProperties.getRabbitmqPort().isEmpty()) {
                        connectionFactory.setPort(Integer.parseInt(myMqProperties.getRabbitmqPort()));
                    }
                }
            }
        }
        return connectionFactory;
    }

    public static void send(String exchange, String routingKey, String message) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = getConnectionFactory().newConnection();
            channel = connection.createChannel();
            send(channel, exchange, routingKey, message);
        } catch (Exception e) {

        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void send(Channel channel, String exchange, String routingKey, String message) {
        try {
            channel.basicPublish(exchange, routingKey, null, message.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
