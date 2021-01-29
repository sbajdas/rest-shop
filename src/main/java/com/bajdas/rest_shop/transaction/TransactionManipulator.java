package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.Product;
import com.bajdas.rest_shop.model.ProductQuantity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TransactionManipulator {

  ClientTransaction startNew(BigDecimal quantity, Product product) {
    var transaction = new ClientTransaction();
    transaction.addItem(product, quantity);
    return transaction;
  }

  ClientTransaction addItem(ClientTransaction transaction, Product product, BigDecimal quantity) {
    Optional<ProductQuantity> existingItem = transaction.getItems().stream()
        .filter(item -> item.containsProduct(product))
        .findFirst();
    if (existingItem.isPresent()) {
      existingItem.get().addQuantityToProduct(quantity);
    } else {
      transaction.addItem(product, quantity);
    }
    return transaction;
  }
}