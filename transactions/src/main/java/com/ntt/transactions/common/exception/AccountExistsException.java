package com.ntt.transactions.common.exception;

public class AccountExistsException extends RuntimeException {

  public AccountExistsException(String message) {
    super(message);
  }
}
