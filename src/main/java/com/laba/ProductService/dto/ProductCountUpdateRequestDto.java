
package com.laba.ProductService.dto;

import lombok.Builder;

@Builder
public record ProductCountUpdateRequestDto(Long id, int numberOfProduct) {

}
