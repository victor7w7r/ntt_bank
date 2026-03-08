package com.ntt.customers.infrastructure.config;

import com.ntt.customers.domain.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalControllerAdviceTest {

  @InjectMocks
  private GlobalControllerAdvice globalControllerAdvice;

  @Test
  public void globalControllerAdviceTest_handleBadRequest() {
    final var exception = new CustomerNotFoundException(
            "ERROR: Cliente no encontrado"
    );
    final var response = globalControllerAdvice.handleBadRequest(exception);

    assertEquals(
            HttpStatus.BAD_REQUEST.value(),
            response.getStatusCodeValue()
    );
    assertEquals("ERROR: Cliente no encontrado", response.getBody());
  }

  @Test
  public void globalControllerAdviceTest_handleGeneral() {
    final var exception = new Exception("ERROR: Cliente no encontrado");
    final var response = globalControllerAdvice.handleGeneral(exception);

    assertEquals(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            response.getStatusCodeValue()
    );
    assertEquals("Error interno del servidor", response.getBody());
  }
}
