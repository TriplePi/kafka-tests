package com.triplepi.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.Map;

@EnableIntegration
@Configuration
public class ConsumerChannelConfig {

    private static final String BROKER_ADDRESS = "localhost:9092";

    @Bean
    public KafkaMessageListenerContainer<String,String> kafkaMessageListenerContainer() {
        return new KafkaMessageListenerContainer<>(kafkaConsumerFactory(), containerProperties());
    }

    @Bean
    public DefaultKafkaConsumerFactory<String,String> kafkaConsumerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_ADDRESS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ContainerProperties containerProperties() {
        ContainerProperties containerProperties = new ContainerProperties("testOutput");
        containerProperties.setGroupId("consumerGroup");
        return containerProperties;
    }

    @Bean
    public KafkaMessageDrivenChannelAdapter<String,String> channelAdapter(){
        return new KafkaMessageDrivenChannelAdapter<>(kafkaMessageListenerContainer());
    }

    @Bean
    public TestConsumer testConsumer(){
        return new TestConsumer();
    }

    @Bean
    public IntegrationFlow generateInnerInboundRequestsFLow() {
        return IntegrationFlows.from(channelAdapter())
                .handle(testConsumer().processor())
                .get();
    }
}
