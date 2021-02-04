package com.bajdas.rest_shop.notification;

import com.bajdas.rest_shop.model.ClientTransactionDto;

public interface Observer {
  void update(Status status, ClientTransactionDto transaction);
}
