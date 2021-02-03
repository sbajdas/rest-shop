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
public class TransactionFacade {
  private TransactionManipulator transactionManipulator;
  private ProductFinder productFinder;
  private TransactionRepository transactionRepository;
  private TotalPriceCalculator priceCalculator;

  @Autowired
  public TransactionFacade(ProductFinder productFinder, TransactionRepository transactionRepository, TransactionManipulator transactionManipulator, TotalPriceCalculator priceCalculator) {
    this.productFinder = productFinder;
    this.transactionRepository = transactionRepository;
    this.transactionManipulator = transactionManipulator;
    this.priceCalculator = priceCalculator;
  }

  ClientTransactionDto createTransaction(Long productId, BigDecimal quantity) {
    var product = productFinder.findProduct(productId);
    var transaction = transactionManipulator.startNew(quantity, product);
    var transactionDto = saveChangesAndRetrieveDtoWithPrice(transaction);
    log.info("New transaction with id {}. Product id: {}, quantity: {}", transactionDto.getTransactionId(), productId, quantity);
    return transactionDto;
  }

  ClientTransactionDto addToTransaction(Long transactionId, Long productId, BigDecimal quantity) {
    var product = productFinder.findProduct(productId);
    var transaction = findTransaction(transactionId);
    transaction = transactionManipulator.addItem(transaction, product, quantity);
    var transactionDto = saveChangesAndRetrieveDtoWithPrice(transaction);
    log.info("Item added to transaction with id {}. Product id: {}, quantity: {}", transactionId, productId, quantity);
    return transactionDto;
  }

  ClientTransactionDto finishTransaction(Long transactionId) {
    var transaction = findTransaction(transactionId);
    transaction.setCompleted(true);
    var transactionDto = saveChangesAndRetrieveDtoWithPrice(transaction);
    log.info("Transaction with id {} completed.", transactionId);
    //TODO: send info via SNS/SQS
    return transactionDto;
  }

  private ClientTransactionDto saveChangesAndRetrieveDtoWithPrice(ClientTransaction transaction) {
    var saved = transactionRepository.save(transaction);
    var totalPrice = priceCalculator.calculate(saved.getItems());
    return TransactionDtoTranslator.translate(saved, totalPrice);
  }

  private ClientTransaction findTransaction(Long transactionId) {
    var transaction = transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
    if(transaction.isCompleted()) {
      throw new TransactionCompletedException();
    }
    return transaction;
  }
}
