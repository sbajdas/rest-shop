package com.bajdas.restshop.transaction;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Transaction already completed")
class TransactionCompletedException extends RuntimeException {
}
