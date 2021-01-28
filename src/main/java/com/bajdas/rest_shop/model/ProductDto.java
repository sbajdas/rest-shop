package com.bajdas.rest_shop.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductDto {
  Product product;
  int quantity;
  BigDecimal totalPrice;
}
