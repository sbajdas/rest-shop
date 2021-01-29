package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.ClientTransactionDto;

import java.math.BigDecimal;

class TransactionDtoTranslator {
  private TransactionDtoTranslator() {
  }

  static ClientTransactionDto translate(ClientTransaction transaction, BigDecimal totalPrice) {
    return ClientTransactionDto.builder()
        .transactionId(transaction.getTransactionId())
        .items(transaction.getItems())
        .totalPrice(totalPrice)
        .build();
  }
}
