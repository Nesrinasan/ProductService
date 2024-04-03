package com.laba.ProductService.job;

import com.laba.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProductJob {

    @Value(value = "${product.category}")
    private String category;

    private final ProductService productService;

    public ProductJob(ProductService productService) {
        this.productService = productService;
    }

  //  @Scheduled(cron = "0 0 0/1 1/1 * ? *")
    @Scheduled(fixedDelay = 10000)
    public void updateProduct(){
        productService.productListByCategoryUpdate(category);

    }

}
