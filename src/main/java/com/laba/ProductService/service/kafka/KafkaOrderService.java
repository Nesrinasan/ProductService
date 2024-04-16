package com.laba.ProductService.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laba.ProductService.dto.kafka.UpdateProductStopDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaOrderService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Value("${topic.stock.update_fail}")
    private String stockUpdateFailTopic;

    public KafkaOrderService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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
