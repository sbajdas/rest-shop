package com.bajdas.restshop.transaction;

import com.bajdas.restshop.model.ClientTransaction;
import com.bajdas.restshop.model.ClientTransactionDto;
import com.bajdas.restshop.model.ProductBasketDto;
import com.bajdas.restshop.notification.Observable;
import com.bajdas.restshop.notification.Observer;
import com.bajdas.restshop.notification.Status;
import com.bajdas.restshop.product.ProductService;
import com.bajdas.restshop.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  ClientTransactionDto createTransaction(List<ProductBasketDto> itemList) {
    var transaction = transactionManipulator.startNew();
    itemList.forEach(item -> addItemToTransaction(transaction, item));
    var transactionDto = saveChangesAndCalculateTotalPrice(transaction);
    notifyObservers(Status.NEW_TRANSACTION, transactionDto);
    return transactionDto;
  }

  ClientTransactionDto addToTransaction(Long transactionId, List<ProductBasketDto> itemList) {
    var transaction = findOpenTransaction(transactionId);
    itemList.forEach(item -> addItemToTransaction(transaction, item));
    var transactionDto = saveChangesAndCalculateTotalPrice(transaction);
    notifyObservers(Status.ADD_ITEMS, transactionDto);
    return transactionDto;
  }

  ClientTransactionDto finishTransaction(Long transactionId) {
    var transaction = findOpenTransaction(transactionId);
    transactionManipulator.finish(transaction);
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

  private void addItemToTransaction(ClientTransaction transaction, ProductBasketDto item) {
    var product = productService.findProduct(item.getProductId());
    transactionManipulator.addItem(transaction, product, item.getQuantity());
    log.info("Item added to transaction with id {}. Product id: {}, quantity: {}", transaction.getTransactionId(), item.getProductId(), item.getQuantity());
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
