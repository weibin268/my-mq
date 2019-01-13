package com.zhuang.mq.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyMqProperties {

    private Properties properties;
    public final static String DEFAULT_CONFIG_FILE_PATH = "config/my-mq.properties";
    private final static String RABBITMQ_HOST = "my.mq.rabbitmq-host";
    private final static String RABBITMQ_PORT = "my.mq.rabbitmq-port";
    private final static String RABBITMQ_USERNAME = "my.mq.rabbitmq-username";
    private final static String RABBITMQ_PASSWORD = "my.mq.rabbitmq-password";

    private volatile static MyMqProperties myMqProperties;

    public static MyMqProperties getInstance() {
        if (myMqProperties == null) {
            synchronized (MyMqProperties.class) {
                if (myMqProperties == null) {
                    myMqProperties = new MyMqProperties();
                }
            }
        }
        return myMqProperties;
    }


    public MyMqProperties() {
        this(DEFAULT_CONFIG_FILE_PATH);
    }

    public MyMqProperties(String configFile) {
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
            properties = new Properties();
            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("加载“my-mq.properties”配置文件出错！");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    public String getRabbitmqHost() {
        return properties.getProperty(RABBITMQ_HOST);
    }

    public String getRabbitmqPort() {
        return properties.getProperty(RABBITMQ_PORT);
    }

    public String getRabbitmqUsername() {
        return properties.getProperty(RABBITMQ_USERNAME);
    }

    public String getRabbitmqPassword() {
        return properties.getProperty(RABBITMQ_PASSWORD);
    }
}
