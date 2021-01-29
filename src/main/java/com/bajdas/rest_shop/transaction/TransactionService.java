package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.ClientTransactionDto;
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
  private TotalPriceCalculator priceCalculator;

  @Autowired
  public TransactionService(ProductFinder productFinder, TransactionRepository transactionRepository, TransactionManipulator transactionManipulator, TotalPriceCalculator priceCalculator) {
    this.productFinder = productFinder;
    this.transactionRepository = transactionRepository;
    this.transactionManipulator = transactionManipulator;
    this.priceCalculator = priceCalculator;
  }

  ClientTransactionDto addToTransaction(Long transactionId, Long productId, BigDecimal quantity) {
    var product = productFinder.findProduct(productId);
    var transaction = findTransaction(transactionId);
    transaction = transactionManipulator.addItem(transaction, product, quantity);
    var saved = transactionRepository.save(transaction);
    var totalPrice = priceCalculator.calculate(saved.getItems());
    log.info("Item added to transaction with id {}. Product id: {}, quantity: {}", transactionId, productId, quantity);
    return TransactionDtoTranslator.translate(saved, totalPrice);
  }

  ClientTransactionDto createTransaction(Long productId, BigDecimal quantity) {
    var product = productFinder.findProduct(productId);
    var transaction = transactionManipulator.startNew(quantity, product);
    var saved = transactionRepository.save(transaction);
    var totalPrice = priceCalculator.calculate(saved.getItems());
    log.info("New transaction with id {}. Product id: {}, quantity: {}", saved.getTransactionId(), productId, quantity);
    return TransactionDtoTranslator.translate(saved, totalPrice);
  }

  private ClientTransaction findTransaction(Long transactionId) {
    return transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
  }
}
