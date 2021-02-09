package com.bajdas.restshop.transaction;

import com.bajdas.restshop.model.ClientTransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  //TODO: accept JSON with more entries
  @PostMapping("/addToBasket")
  public ResponseEntity<ClientTransactionDto> addToBasket(@RequestParam(required = false) Long transactionId, @RequestParam Long productId, @RequestParam double quantity) {
    ClientTransactionDto storedTransaction;
    var productQuantity = BigDecimal.valueOf(quantity);
    if(Objects.nonNull(transactionId)) {
      storedTransaction = transactionService.addToTransaction(transactionId, productId, productQuantity);
    } else {
      storedTransaction = transactionService.createTransaction(productId, productQuantity);
    }
    return ResponseEntity.ok(storedTransaction);
  }

  @GetMapping("/finishTransaction")
  public ResponseEntity<ClientTransactionDto> finishTransaction(@RequestParam Long transactionId) {
    ClientTransactionDto storedTransaction = transactionService.finishTransaction(transactionId);
    return ResponseEntity.ok(storedTransaction);
  }

  //TODO: meaningful error messages
}