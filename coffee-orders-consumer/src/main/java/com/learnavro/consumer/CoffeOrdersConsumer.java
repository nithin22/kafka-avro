package com.learnavro.consumer;


import com.learnavro.domain.generated.CoffeeOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CoffeOrdersConsumer {

    @KafkaListener(topics = "coffee-orders",groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<String, GenericRecord> consumerRecord){
        log.info("Consumed Record: {}, value {}",consumerRecord.key(),consumerRecord.value().toString());
    }
}
