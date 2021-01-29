package com.bajdas.rest_shop.repository;

import com.bajdas.rest_shop.model.ClientTransaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<ClientTransaction, Long> {

}
