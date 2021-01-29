package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  @PostMapping("/addToBasket")
  public ResponseEntity<ClientTransaction> addToBasket(@RequestParam(required = false) Long transactionId, @RequestParam Long productId, @RequestParam double quantity) {
    ClientTransaction storedTransaction;
    var productQuantity = BigDecimal.valueOf(quantity);
    if(Objects.nonNull(transactionId)) {
      storedTransaction = transactionService.addToTransaction(transactionId, productId, productQuantity);
    } else {
      storedTransaction = transactionService.createTransaction(productId, productQuantity);
    }
    return ResponseEntity.ok(storedTransaction);
  }

  //TODO: Finish transaction, send info via SNS/SQS?
}
