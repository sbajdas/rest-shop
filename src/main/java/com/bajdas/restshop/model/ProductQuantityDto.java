package com.bajdas.restshop.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public class ProductQuantityDto {
  Product item;
  BigDecimal quantity;

  public static ProductQuantityDto fromProductQuantity(ProductQuantity from) {
    return ProductQuantityDto.builder()
        .item(from.getItem())
        .quantity(from.getQuantity())
        .build();
  }
}
