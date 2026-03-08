package com.ntt.transactions.common.exception;

public class MovementNotFoundException extends RuntimeException {

  public MovementNotFoundException(String message) {
    super(message);
  }
}
