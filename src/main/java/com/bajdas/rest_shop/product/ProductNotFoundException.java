package com.bajdas.rest_shop.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "product not found")
public class ProductNotFoundException extends RuntimeException {
}
