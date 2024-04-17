package com.laba.ProductService.service.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Value("${topic.stock.update_fail}")
    private String stockUpdateFailTopic;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageKafkaStockKontroFail(String orderNumber){

        try {
            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(stockUpdateFailTopic, orderNumber);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + orderNumber +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            orderNumber + "] due to : " + ex.getMessage());
                }
            });

        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }

}
