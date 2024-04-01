
package com.laba.ProductService.repository;


import com.laba.ProductService.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAllByCategory(String category);


}
