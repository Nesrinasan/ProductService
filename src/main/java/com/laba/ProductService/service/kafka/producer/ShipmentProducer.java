package com.laba.ProductService.service.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laba.ProductService.dto.kafka.CreateOrderDto;
import com.laba.ProductService.dto.kafka.ShipmentCargoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ShipmentProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final OrderProducer kafkaOrderService;


    @Value("${topic.shipment.create}")
    private String shipmentOrderTopic;

    public ShipmentProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, OrderProducer kafkaOrderService) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.kafkaOrderService = kafkaOrderService;
    }

    public void sendMessageKafkaShipment(CreateOrderDto createOrderDto){

        try {
            ShipmentCargoDto shipmentCargoDto = new ShipmentCargoDto(createOrderDto.orderNumber(), createOrderDto.productId());
            String updateProductStopDtoStr = objectMapper.writeValueAsString(shipmentCargoDto);

            CompletableFuture<SendResult<String, String>> sendResultCompletableFuture = kafkaTemplate.send(shipmentOrderTopic, updateProductStopDtoStr);
            sendResultCompletableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + createOrderDto +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    throw new RuntimeException("Ürünün  kargo aşamasında bir sorun oluştu.");

                }
            });

        }catch (Exception e){
            log.error("Message is not sent ", e);
        }

    }
}
