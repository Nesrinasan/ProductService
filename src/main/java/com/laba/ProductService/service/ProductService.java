
package com.laba.ProductService.service;


import com.laba.ProductService.dto.*;
import com.laba.ProductService.exception.GeneralException;
import com.laba.ProductService.model.Product;
import com.laba.ProductService.repository.ProductRepository;
import com.laba.ProductService.service.kafka.producer.ProductProducer;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final ProductProducer productProducer;


    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, ProductProducer productProducer) {
        this.productRepository = productRepository;

        this.modelMapper = modelMapper;
        this.productProducer = productProducer;
    }

    public void save(ProductSaveReqestDto productSaveReqestDto) {
        Product product = modelMapper.map(productSaveReqestDto, Product.class);
        product.setCategory(productSaveReqestDto.getCategory());
        product.setName(productSaveReqestDto.getName());
        saveProduct(product);

    }

    public void updateProducts(ProductUpdateRequestDto productUpdateRequestDto) {

        productProducer.sendMessageKafkaUpdateProducts(productUpdateRequestDto);


    }

    public void updateProductCount(ProductCountUpdateRequestDto productCountUpdateRequestDto) {

        Optional<Product> productOptional = productRepository.findById(productCountUpdateRequestDto.id());
        Product product = productOptional.orElseThrow(GeneralException::new);
        product.setNumberOfProduct(productCountUpdateRequestDto.numberOfProduct());
        saveProduct(product);

    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);


    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).get();
    }


    public void delete(Long productId) {
        Product product = productRepository.findById(productId).get();
        productRepository.delete(product);

    }

    public Optional<ProductInfoResponseDto> info(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(GeneralException::new);
        return Optional.ofNullable(modelMapper.map(product, ProductInfoResponseDto.class));

    }

    @Cacheable(value = "productsByCategory3", key = "#category", cacheManager = "cManager")
    public Optional<List<ProductResponseByCategoryDto>> productListByCategory(String category) {
        List<Product> allByCategory = productRepository.findAllByCategory(category);

        List<ProductResponseByCategoryDto> list = allByCategory.stream().map(product -> {

            return modelMapper.map(product, ProductResponseByCategoryDto.class);

        }).toList();

        return Optional.ofNullable(list);

    }


    @CachePut(value = "productsByCategory3", key = "#category", cacheManager = "cManager")
    public Optional<List<ProductResponseByCategoryDto>> productListByCategoryUpdate(String category) {
        List<Product> allByCategory = productRepository.findAllByCategory(category);

        List<ProductResponseByCategoryDto> list = allByCategory.stream().map(product -> {

            return modelMapper.map(product, ProductResponseByCategoryDto.class);

        }).toList();

        return Optional.ofNullable(list);

    }

    @CacheEvict(value = "productsByCategory2", key = "#category")
    public void delete() {
        System.out.println();
    }

}
