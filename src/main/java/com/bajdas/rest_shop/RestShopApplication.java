package com.bajdas.rest_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RestShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestShopApplication.class, args);
	}

}
