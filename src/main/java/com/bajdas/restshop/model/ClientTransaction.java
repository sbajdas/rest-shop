package com.bajdas.restshop.model;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ClientTransaction {

  @Id
  @GeneratedValue
  private Long transactionId;
  private boolean completed;
  //TODO: Add Client concept
  @Embedded
  @ElementCollection
  List<ProductQuantity> items = new ArrayList<>();

  public void addItem(Product product, BigDecimal quantity) {
    items.add(new ProductQuantity(product, quantity));
  }


}
