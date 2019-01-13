package com.zhuang.mq.util;

import com.rabbitmq.client.*;
import com.zhuang.mq.config.MyMqProperties;
import com.zhuang.mq.handler.ReceiveHandler;

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

    public static Channel createChannel() {
        try {
            return getConnectionFactory().newConnection().createChannel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public static void receive(Channel channel, String queue, ReceiveHandler receiveHandler) {
        Consumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                receiveHandler.receive(message);
            }
        };
        try {
            channel.basicConsume(queue, true, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
