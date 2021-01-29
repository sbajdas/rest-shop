package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.product.ProductFinder;
import com.bajdas.rest_shop.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class TransactionService {
  private TransactionManipulator transactionManipulator;
  private ProductFinder productFinder;
  private TransactionRepository transactionRepository;

  @Autowired
  public TransactionService(ProductFinder productFinder, TransactionRepository transactionRepository, TransactionManipulator transactionManipulator) {
    this.productFinder = productFinder;
    this.transactionRepository = transactionRepository;
    this.transactionManipulator = transactionManipulator;
  }

  ClientTransaction addToTransaction(Long transactionId, Long productId, BigDecimal quantity) {
    var product = productFinder.findProduct(productId);
    var transaction = findTransaction(transactionId);
    transaction = transactionManipulator.addItem(transaction, product, quantity);
    ClientTransaction saved = transactionRepository.save(transaction);
    //TODO: Calculate total price
    //TODO: Return DTO with total price
    log.info("Item added to transaction with id {}. Product id: {}, quantity: {}", transactionId, productId, quantity);
    return saved;
  }

  ClientTransaction createTransaction(Long productId, BigDecimal quantity) {
    var product = productFinder.findProduct(productId);
    var transaction = transactionManipulator.startNew(quantity, product);
    ClientTransaction saved = transactionRepository.save(transaction);
    log.info("New transaction with id {}. Product id: {}, quantity: {}", saved.getTransactionId(), productId, quantity);
    return saved;
  }

  private ClientTransaction findTransaction(Long transactionId) {
    return transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
  }
}
