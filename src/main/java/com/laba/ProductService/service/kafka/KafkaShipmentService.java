package com.laba.ProductService.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laba.ProductService.dto.kafka.ShipmentCargoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaShipmentService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final KafkaOrderService kafkaOrderService;


    @Value("${topic.stock.update}")
    private String stockUpdateTopic;

    public KafkaShipmentService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, KafkaOrderService kafkaOrderService) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.kafkaOrderService = kafkaOrderService;
    }

    public void sendMessageKafkaShipment(String orderNumber){

        try {
            ShipmentCargoDto shipmentCargoDto = new ShipmentCargoDto(orderNumber);
            String updateProductStopDtoStr = objectMapper.writeValueAsString(shipmentCargoDto);

            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(stockUpdateTopic, updateProductStopDtoStr);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + orderNumber +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    throw new RuntimeException("Ürünün  kargo aşamasında bir sorun oluştu.");
                 //   kafkaOrderService.sendMessageKafkaStockKontroFail(orderNumber);
                }
            });

        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }
}
