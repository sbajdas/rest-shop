package com.bajdas.rest_shop.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class ProductAmount {
//  @ManyToMany
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;
  @Column(nullable = false)
  private Long amount;
}
