package com.bajdas.rest_shop.transaction;

import com.bajdas.rest_shop.model.ClientBasket;
import com.bajdas.rest_shop.model.Product;
import com.bajdas.rest_shop.model.ProductDto;
import com.bajdas.rest_shop.product.ProductFinder;
import com.bajdas.rest_shop.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {
  @Autowired
  private ProductFinder productFinder;
  @Autowired
  private TransactionRepository transactionRepository;

  public ProductDto addToBasket(Long id, int quantity) {
    Product product = productFinder.findProduct(id);
    BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    return new ProductDto(product, quantity, totalPrice);
  }

  ClientBasket addToBasket(Long basketId, Long productId, Long quantity) {
    var basket = Optional.ofNullable(basketId)
        .map(transactionRepository::findById)
        .map(Optional::get)
        .orElseGet(ClientBasket::new);
    var product = productFinder.findProduct(productId);
    return add(basket, product, quantity);
  }

  private ClientBasket add(ClientBasket clientBasket, Product product, Long quantity) {
    clientBasket.addProductAmount(product, quantity);
    return transactionRepository.save(clientBasket);
  }
}
