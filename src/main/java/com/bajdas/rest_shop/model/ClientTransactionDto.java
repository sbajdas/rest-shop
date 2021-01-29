package com.bajdas.rest_shop.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Value
public class ClientTransactionDto {
  Long transactionId;
  List<ProductQuantity> items;
  BigDecimal totalPrice;
}
