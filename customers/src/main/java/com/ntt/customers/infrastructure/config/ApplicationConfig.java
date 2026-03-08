package com.ntt.customers.infrastructure.config;

import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.infrastructure.in.messaging.CustomerMessagingInputAdapter;
import com.ntt.customers.infrastructure.out.messaging.CustomerMessagingOutputAdapter;
import com.ntt.customers.infrastructure.out.messaging.mapper.CustomerMessagingOutputMapper;
import com.ntt.customers.infrastructure.out.persistence.CustomerRepositoryPersistenceAdapter;
import com.ntt.customers.infrastructure.out.persistence.mapper.CustomerPersistenceMapper;
import com.ntt.customers.infrastructure.out.persistence.repository.CustomerRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
