package com.bajdas.restshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductBasketDto {
  Long productId;
  BigDecimal quantity;
}
