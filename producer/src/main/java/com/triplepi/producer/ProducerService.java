package com.triplepi.producer;

import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    @Publisher(channel = "toKafka")
    public String publish(String message) {
        return message;
    }

}
