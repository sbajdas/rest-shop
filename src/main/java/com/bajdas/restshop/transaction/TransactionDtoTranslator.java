package com.bajdas.restshop.transaction;

import com.bajdas.restshop.model.ClientTransaction;
import com.bajdas.restshop.model.ClientTransactionDto;
import com.bajdas.restshop.model.ProductQuantityDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

class TransactionDtoTranslator {
  private TransactionDtoTranslator() {
  }

  static ClientTransactionDto translate(ClientTransaction transaction, BigDecimal totalPrice) {
    return ClientTransactionDto.builder()
        .transactionId(transaction.getTransactionId())
        .items(translateItems(transaction))
        .totalPrice(totalPrice)
        .build();
  }

  private static List<ProductQuantityDto> translateItems(ClientTransaction transaction) {
    return transaction.getItems()
        .stream()
        .map(ProductQuantityDto::fromProductQuantity)
        .collect(Collectors.toList());
  }
}
