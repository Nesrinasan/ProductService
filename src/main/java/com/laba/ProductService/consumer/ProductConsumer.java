package com.laba.ProductService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laba.ProductService.dto.kafka.CreateOrderDto;
import com.laba.ProductService.model.Product;
import com.laba.ProductService.service.ProductService;
import com.laba.ProductService.service.kafka.KafkaOrderService;
import com.laba.ProductService.service.kafka.KafkaShipmentService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class ProductConsumer {

    private final ObjectMapper objectMapper;
    private final ProductService productService;

    private final KafkaShipmentService kafkaShipmentService;

    private final KafkaOrderService kafkaOrderService;

    public ProductConsumer(ObjectMapper objectMapper, ProductService productService, KafkaShipmentService kafkaShipmentService, KafkaOrderService kafkaOrderService) {
        this.objectMapper = objectMapper;
        this.productService = productService;
        this.kafkaShipmentService = kafkaShipmentService;
        this.kafkaOrderService = kafkaOrderService;
    }

    @KafkaListener(topics = "${topic.createOrder}", groupId = "myGroup")
    @RetryableTopic(
            attempts = "2",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    public void listen(String message) {
        CreateOrderDto createOrderDto = null;
        try {
            createOrderDto = objectMapper.readValue(message, CreateOrderDto.class);
            Long productId = createOrderDto.productId();
            Product productById = productService.findProductById(productId);

            int numberOfProduct = productById.getNumberOfProduct();
            productById.setNumberOfProduct(--numberOfProduct);
            productService.saveProduct(productById);
            kafkaShipmentService.sendMessageKafkaShipment(createOrderDto.orderNumber());

      //      kafkaProductService.sendMessageKafkaStockKontrol(true, createOrderDto.orderNumber());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            Product productById = productService.findProductById(createOrderDto.productId());
            int numberOfProduct = productById.getNumberOfProduct();
            productById.setNumberOfProduct(++numberOfProduct);
            productService.saveProduct(productById);
            kafkaOrderService.sendMessageKafkaStockKontroFail(createOrderDto.orderNumber());

        }
    }

    @DltHandler
    public void handleDltPayment(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println();

    }
}
