package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ProductQuantity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
class TotalPriceCalculator {

  BigDecimal calculate(List<ProductQuantity> items) {
    return items.stream()
        .map(pq -> pq.getItem().getPrice().multiply(pq.getQuantity()))
        .map(price -> price.setScale(2, RoundingMode.HALF_UP))
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }
}
