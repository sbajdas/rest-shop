package com.bajdas.restshop.product;

import com.bajdas.restshop.model.Product;
import com.bajdas.restshop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

  private ProductRepository repository;

  @Autowired
  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  @Cacheable("findProduct")
  Product findProduct(Long id, String name) {
    log.info("Product search not from cache: id {}, name: {}", id, name);
    Optional<Product> product = (id != null) ? repository.findById(id) : repository.findByName(name);
    return product.orElseThrow(ProductNotFoundException::new);
  }

  @Cacheable("findAllProducts")
  List<Product> findAllProducts() {
    return repository.findAll();
  }

  @Cacheable("findProductById")
  public Product findProduct(Long id) {
    log.info("Product search not from cache: id {}", id);
    return repository.findById(id)
        .orElseThrow(ProductNotFoundException::new);
  }
}
