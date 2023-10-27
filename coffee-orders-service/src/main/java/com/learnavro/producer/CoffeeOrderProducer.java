package com.learnavro.producer;

import com.learnavro.domain.generated.CoffeeOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class CoffeeOrderProducer {

    @Autowired
    KafkaTemplate<String, CoffeeOrder>kafkaTemplate;

    public void sendMessage(CoffeeOrder coffeeOrderAvro){
        var producerRecord=new ProducerRecord<>("coffee-orders",coffeeOrderAvro.getId().toString(),coffeeOrderAvro);
        ListenableFuture<SendResult<String,CoffeeOrder>> listenableFuture=kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, CoffeeOrder>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(coffeeOrderAvro,ex);
            }

            @Override
            public void onSuccess(SendResult<String, CoffeeOrder> result) {
                handleSuccess(coffeeOrderAvro,result);
            }
        });

    }

    private void handleFailure(CoffeeOrder coffeeOrderAvro, Throwable ex) {

        log.error("Error sending the message for {}  and the exceptioin is:{}",coffeeOrderAvro,ex.getMessage(),ex);
    }

    private void handleSuccess(CoffeeOrder coffeeOrderAvro, SendResult<String, CoffeeOrder> result) {
        log.info("Message sent successfully for thr key:{}, and the value is {}"+",partition is {}",
                coffeeOrderAvro.getId(),coffeeOrderAvro,result.getRecordMetadata().partition());
    }


}
