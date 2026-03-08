package com.ntt.customers.domain.exception;

public class CustomerNotFoundException extends RuntimeException {

  public CustomerNotFoundException(String message) {
    super(message);
  }
}
