package com.bajdas.restshop.notification;

import com.bajdas.restshop.model.ClientTransactionDto;

public interface Observable {
  //TODO: to be deleted?
  void registerObserver(Observer observer);
  void notifyObservers(Status status, ClientTransactionDto transaction);
}
