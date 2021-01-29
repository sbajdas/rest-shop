package com.bajdas.rest_shop.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "transaction not found")
public class TransactionNotFoundException extends RuntimeException {
}
