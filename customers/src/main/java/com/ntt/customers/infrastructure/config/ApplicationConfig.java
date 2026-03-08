package com.ntt.customers.infrastructure.config;

import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.infrastructure.in.messaging.CustomerMessagingInputAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public CustomerMessagingInputAdapter customerMessagingInputAdapter(
          CustomerSearchUseCase customerUseCase
  ) {
    return new CustomerMessagingInputAdapter(customerUseCase);
  }
}
