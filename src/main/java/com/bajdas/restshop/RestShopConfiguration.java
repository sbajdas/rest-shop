package com.bajdas.restshop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
class RestShopConfiguration {

  @Bean
  SnsClient snsClient() {
    return SnsClient.create();
  }
}
