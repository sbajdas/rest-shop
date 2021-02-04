package com.bajdas.rest_shop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class RestShopConfiguration {

  @Bean
  SnsClient snsClient() {
    return SnsClient.create();
  }
}
