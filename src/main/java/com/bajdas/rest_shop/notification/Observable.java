package com.bajdas.rest_shop.notification;

import com.bajdas.rest_shop.model.ClientTransactionDto;

public interface Observable {
  void registerObserver(Observer observer);
  void notifyObservers(Status status, ClientTransactionDto transaction);
}
