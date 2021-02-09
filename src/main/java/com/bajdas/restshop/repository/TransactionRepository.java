package com.bajdas.restshop.repository;

import com.bajdas.restshop.model.ClientTransaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<ClientTransaction, Long> {

}
