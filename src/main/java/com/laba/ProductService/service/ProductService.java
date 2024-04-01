
package com.laba.ProductService.service;


import com.laba.ProductService.dto.ProductCountUpdateRequestDto;
import com.laba.ProductService.dto.ProductInfoResponseDto;
import com.laba.ProductService.dto.ProductResponseByCategoryDto;
import com.laba.ProductService.dto.ProductSaveReqestDto;
import com.laba.ProductService.exception.GeneralException;
import com.laba.ProductService.model.Product;
import com.laba.ProductService.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;

        this.modelMapper = modelMapper;
    }

    public void save(ProductSaveReqestDto productSaveReqestDto){
        Product product =  modelMapper.map(productSaveReqestDto, Product.class);
        product.setCategory(productSaveReqestDto.getCategory());
        product.setName(productSaveReqestDto.getName());
        saveProduct(product);

    }

    public void updateProductCount(ProductCountUpdateRequestDto productCountUpdateRequestDto){

        Optional<Product> productOptional = productRepository.findById(productCountUpdateRequestDto.id());
        Product product = productOptional.orElseThrow(GeneralException::new);
        product.setNumberOfProduct(productCountUpdateRequestDto.numberOfProduct());
        saveProduct(product);

    }

    public void saveProduct(Product product) {
        productRepository.save(product);


    }

    public Product findProductById(Long id){
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

    public Optional<List<ProductResponseByCategoryDto>> productListByCategory(String category) {
        List<Product> allByCategory = productRepository.findAllByCategory(category);

        List<ProductResponseByCategoryDto> list = allByCategory.stream().map(product -> {

            ProductResponseByCategoryDto productResponseByCategoryDto = modelMapper.map(product, ProductResponseByCategoryDto.class);

            return productResponseByCategoryDto;
        }).toList();

        /**
         * Mapstruct
         */
      //  List<ProductResponseByCategoryDto> listMapstruct = allByCategory.stream().map(productMapper::productToProductResponseDtoByCategory).toList();

        return Optional.ofNullable(list);

    }


}
