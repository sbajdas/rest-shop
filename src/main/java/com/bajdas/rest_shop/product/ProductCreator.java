package com.bajdas.rest_shop.product;

import com.bajdas.rest_shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ProductCreator {

  @Autowired
  private ProductRepository repository;

  @Autowired
  private ProductFactory productFactory;


  @PostConstruct
  public void fillProductDatabase() {
    var product1 = productFactory.prepareProduct("ham", 10.34);
    var product2 = productFactory.prepareProduct("beer", 3.14);

    repository.saveAll(List.of(product1,product2));
  }

}
