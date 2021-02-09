package com.bajdas.restshop.notification;

import com.bajdas.restshop.model.ClientTransactionDto;

public interface Observer {
  void update(Status status, ClientTransactionDto transaction);
}
