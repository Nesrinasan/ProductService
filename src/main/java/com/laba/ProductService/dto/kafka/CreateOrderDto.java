package com.laba.ProductService.dto.kafka;

public record CreateOrderDto(String orderDesc, String orderNumber, Long userId, Long productId) {

}
