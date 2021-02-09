package com.bajdas.restshop.transaction;

import com.bajdas.restshop.model.ClientTransaction;
import com.bajdas.restshop.model.ClientTransactionDto;
import com.bajdas.restshop.notification.Observable;
import com.bajdas.restshop.notification.Observer;
import com.bajdas.restshop.notification.Status;
import com.bajdas.restshop.product.ProductService;
import com.bajdas.restshop.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class TransactionService implements Observable {
  private TransactionManipulator transactionManipulator;
  private ProductService productService;
  private TransactionRepository transactionRepository;
  private TotalPriceCalculator priceCalculator;
  private List<Observer> observers;

  @Autowired
  public TransactionService(ProductService productService, TransactionRepository transactionRepository,
                            TransactionManipulator transactionManipulator, TotalPriceCalculator priceCalculator, List<Observer> observers) {
    this.productService = productService;
    this.transactionRepository = transactionRepository;
    this.transactionManipulator = transactionManipulator;
    this.priceCalculator = priceCalculator;
    this.observers = observers;
  }

  ClientTransactionDto createTransaction(Long productId, BigDecimal quantity) {
    var product = productService.findProduct(productId);
    var transaction = transactionManipulator.startNew(quantity, product);
    var transactionDto = saveChangesAndCalculateTotalPrice(transaction);
    log.info("New transaction with id {}. Product id: {}, quantity: {}", transactionDto.getTransactionId(), productId, quantity);
    return transactionDto;
  }

  ClientTransactionDto addToTransaction(Long transactionId, Long productId, BigDecimal quantity) {
    var product = productService.findProduct(productId);
    var transaction = findOpenTransaction(transactionId);
    transaction = transactionManipulator.addItem(transaction, product, quantity);
    var transactionDto = saveChangesAndCalculateTotalPrice(transaction);
    log.info("Item added to transaction with id {}. Product id: {}, quantity: {}", transactionId, productId, quantity);
    return transactionDto;
  }

  ClientTransactionDto finishTransaction(Long transactionId) {
    var transaction = findOpenTransaction(transactionId);
    transaction.setCompleted(true);
    var transactionDto = saveChangesAndCalculateTotalPrice(transaction);
    log.info("Transaction with id {} completed.", transactionId);
    notifyObservers(Status.TRANSACTION_COMPLETED, transactionDto);
    return transactionDto;
  }

  private ClientTransactionDto saveChangesAndCalculateTotalPrice(ClientTransaction transaction) {
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
