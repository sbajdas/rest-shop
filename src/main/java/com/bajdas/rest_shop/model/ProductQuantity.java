package com.bajdas.rest_shop.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantity {
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product item;
  @Column(nullable = false)
  private BigDecimal quantity;

  public void addQuantityToProduct(BigDecimal newQuantity) {
    quantity = quantity.add(newQuantity);
  }

  public boolean containsProduct(Product product) {
    return item.equals(product);
  }
}
