package com.bajdas.restshop.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Value
public class ClientTransactionDto {
  Long transactionId;
  List<ProductQuantityDto> items;
  BigDecimal totalPrice;
}
