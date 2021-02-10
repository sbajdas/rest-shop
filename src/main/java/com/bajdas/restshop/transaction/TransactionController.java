package com.bajdas.restshop.transaction;

import com.bajdas.restshop.model.ClientTransactionDto;
import com.bajdas.restshop.model.ProductBasketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class TransactionController {
  private TransactionService transactionService;

  @Autowired
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/addToBasket")
  public ResponseEntity<ClientTransactionDto> addToBasket(@RequestParam(required = false) Long transactionId, @RequestBody List<ProductBasketDto> itemList) {
    ClientTransactionDto storedTransaction;
    if(Objects.nonNull(transactionId)) {
      storedTransaction = transactionService.addToTransaction(transactionId, itemList);
    } else {
      storedTransaction = transactionService.createTransaction(itemList);
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
