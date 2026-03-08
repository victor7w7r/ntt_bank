package com.ntt.customers.infrastructure.config;

import com.ntt.customers.domain.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<String> handleBadRequest(CustomerNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ex.getMessage()
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneral(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            "Error interno del servidor"
    );
  }
}
