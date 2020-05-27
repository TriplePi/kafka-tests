package com.triplepi.consumer;

import org.springframework.messaging.MessageHandler;


public class TestConsumer {

    public MessageHandler processor() {
        return message -> System.out.println(message.getPayload());
    }
}