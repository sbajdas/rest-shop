package com.bajdas.rest_shop.repository;

import com.bajdas.rest_shop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
  Optional<Product> findByName(String name);

  List<Product> findAll();
}
