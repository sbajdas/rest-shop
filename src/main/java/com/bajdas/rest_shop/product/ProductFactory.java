package com.bajdas.rest_shop.product;

import com.bajdas.rest_shop.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class ProductFactory {
  Product prepareProduct(String name, double price) {
    return Product.builder()
        .name(name)
        .price(BigDecimal.valueOf(price))
        .build();
  }
}
