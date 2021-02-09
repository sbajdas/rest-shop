package com.bajdas.restshop.product;

import com.bajdas.restshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/productInfo")
  public ResponseEntity<Product> getProductInfo(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) Long id) {
    if(name == null && id == null) {
      return ResponseEntity.badRequest().build();
    }
      var product = productService.findProduct(id, name);
      return ResponseEntity.ok(product);
  }

  @GetMapping("/products")
  @Cacheable("products")
  public ResponseEntity<List<Product>> getProducts() {
      return ResponseEntity.ok(productService.findAllProducts());
  }
}
