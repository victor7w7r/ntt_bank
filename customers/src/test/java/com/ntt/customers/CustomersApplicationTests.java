package com.ntt.customers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CustomersApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
class CustomersApplicationTests {

  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoadsTest_contextLoadsSuccessfully() {
    assertNotNull(context, "El contexto de la aplicación no debe ser nulo");
  }
}
