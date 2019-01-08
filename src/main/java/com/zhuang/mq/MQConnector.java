package com.zhuang.mq;

public interface MQConnector {

	public void connect();
	
	public void send(String message);

	public void close();
	
}
