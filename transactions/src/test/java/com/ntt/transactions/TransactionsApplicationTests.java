package com.ntt.transactions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = TransactionsApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
class TransactionsApplicationTests {

  @Autowired private ApplicationContext context;

  @Test
  void contextLoadsTest_contextLoadsSuccessfully() {
    assertNotNull(context, "El contexto de la aplicación no debe ser nulo");
  }
}
