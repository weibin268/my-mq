package com.zhuang.mq.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.zhuang.mq.MQConnector;

public class ActiveMQConnector implements MQConnector {

	ActiveMQConnectionFactory connectionFactory;

	Connection connection;

	Session session;

	Destination destination;

	MessageProducer producer;

	TargetType targetType;

	String targetName;

	public ActiveMQConnector(String url, String queueName) {
		this(url, ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, TargetType.Queue, queueName);
	}

	public ActiveMQConnector(String url, String userName, String password, TargetType targetType, String targetName) {
		this.connectionFactory = new ActiveMQConnectionFactory(userName, password, url);
		this.targetType = targetType;
	}

	public void connect() {

		try {

			connection = connectionFactory.createConnection();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			if (targetType == TargetType.Queue) {
				destination = session.createQueue(targetName);
			}

			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		} catch (Exception e) {

			throw new RuntimeException("connect出错", e);
		}

	}

	public void close() {

		try {

			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("close出错", e);
		}
	}

	public void send(String message) {

	}

}
