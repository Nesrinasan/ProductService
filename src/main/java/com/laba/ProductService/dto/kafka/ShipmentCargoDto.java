package com.laba.ProductService.dto.kafka;

import java.io.Serializable;

public record ShipmentCargoDto(String orderNumber, long productId) implements Serializable {

}
