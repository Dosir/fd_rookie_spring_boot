package com.fd.rookie.spring.boot.config.rabbit;

import java.util.concurrent.CountDownLatch;

/**
 * 配置消费者
 */
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
