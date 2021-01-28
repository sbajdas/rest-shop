package com.bajdas.rest_shop;

import com.bajdas.rest_shop.product.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ProductController controller;

//  @Test
//  public void greetingShouldReturnDefaultMessage() throws Exception {
//    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
//        String.class)).contains("Hello main!");
//  }

}