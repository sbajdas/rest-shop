package com.bajdas.rest_shop.model;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ClientBasket {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  Long transactionId;
  @Embedded
  @ElementCollection
  List<ProductAmount> productAmount = new ArrayList<>();

  public void addProductAmount(Product product, Long quantity) {
    getProductAmount().add(new ProductAmount(product, quantity));
  }


}
