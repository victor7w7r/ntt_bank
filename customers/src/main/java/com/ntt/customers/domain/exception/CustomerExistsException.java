package com.ntt.customers.domain.exception;

public class CustomerExistsException extends RuntimeException {

  public CustomerExistsException(String message) {
    super(message);
  }
}
