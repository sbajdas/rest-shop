package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientBasket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  @PostMapping("/addToBasket")
  public ResponseEntity<ClientBasket> addToBasket(@RequestParam(required = false) Long basketId, @RequestParam Long productId, @RequestParam Long quantity) {
    ClientBasket returnedBasket = transactionService.addToBasket(basketId, productId, quantity);
    return ResponseEntity.ok(returnedBasket);
  }
}
