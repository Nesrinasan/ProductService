package com.laba.ProductService.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laba.ProductService.dto.ProductSaveReqestDto;
import com.laba.ProductService.dto.ProductUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Value("${topic.update.product}")
    private String saveProductsTopic;

    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageKafkaUpdateProducts(ProductUpdateRequestDto productDto){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String productSaveReqestDtoStr = null;
            try {
                productSaveReqestDtoStr = objectMapper.writeValueAsString(productDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(saveProductsTopic,String.valueOf(productDto.getId()), productSaveReqestDtoStr);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + productDto +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            productDto + "] due to : " + ex.getMessage());
                }
            });

        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }

}
