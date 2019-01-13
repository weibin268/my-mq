package com.zhuang.mq.util;

import junit.framework.TestCase;

public class RabbitMqUtilsTest extends TestCase {

    public void testSend() {

        RabbitMqUtils.send("","test01","zwb");

    }
}