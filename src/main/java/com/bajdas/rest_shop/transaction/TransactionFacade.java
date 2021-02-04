package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientTransaction;
import com.bajdas.rest_shop.model.ClientTransactionDto;
import com.bajdas.rest_shop.notification.Observable;
import com.bajdas.rest_shop.notification.Observer;
import com.bajdas.rest_shop.notification.Status;
import com.bajdas.rest_shop.product.ProductFinder;
import com.bajdas.rest_shop.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TransactionFacade implements Observable {
  private TransactionManipulator transactionManipulator;
  private ProductFinder productFinder;
  private TransactionRepository transactionRepository;
  private TotalPriceCalculator priceCalculator;
  private List<Observer> observers = new ArrayList<>();

  @Autowired
  public TransactionFacade(ProductFinder productFinder, TransactionRepository transactionRepository,
                           TransactionManipulator transactionManipulator, TotalPriceCalculator priceCalculator, List<Observer> observers) {
    this.productFinder = productFinder;
    this.transactionRepository = transactionRepository;
    this.transactionManipulator = transactionManipulator;
    this.priceCalculator = priceCalculator;
    observers.forEach(this::registerObserver);
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
    var transaction = findOpenTransaction(transactionId);
    transaction = transactionManipulator.addItem(transaction, product, quantity);
    var transactionDto = saveChangesAndRetrieveDtoWithPrice(transaction);
    log.info("Item added to transaction with id {}. Product id: {}, quantity: {}", transactionId, productId, quantity);
    return transactionDto;
  }

  ClientTransactionDto finishTransaction(Long transactionId) {
    var transaction = findOpenTransaction(transactionId);
    transaction.setCompleted(true);
    var transactionDto = saveChangesAndRetrieveDtoWithPrice(transaction);
    log.info("Transaction with id {} completed.", transactionId);
    notifyObservers(Status.TRANSACTION_COMPLETED, transactionDto);
    return transactionDto;
  }

  private ClientTransactionDto saveChangesAndRetrieveDtoWithPrice(ClientTransaction transaction) {
    var saved = transactionRepository.save(transaction);
    var totalPrice = priceCalculator.calculate(saved.getItems());
    return TransactionDtoTranslator.translate(saved, totalPrice);
  }

  private ClientTransaction findOpenTransaction(Long transactionId) {
    var transaction = transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
    if(transaction.isCompleted()) {
      throw new TransactionCompletedException();
    }
    return transaction;
  }

  @Override
  public void registerObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void notifyObservers(Status status, ClientTransactionDto transaction) {
    observers.forEach(observer -> observer.update(status, transaction));
  }
}
