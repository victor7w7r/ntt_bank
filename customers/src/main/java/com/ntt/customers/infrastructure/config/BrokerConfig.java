package com.ntt.customers.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokerConfig {

  public static final String EXCHANGE_NAME = "bank-ntt";

  public static final String ACCOUNT_CUSTOMER_QUEUE = "account_customer_queue";
  public static final String ACCOUNT_CUSTOMER_ROUTING_KEY = "created.customer";

  public static final String CUSTOMER_ERASE_QUEUE = "customer_erase";
  public static final String CUSTOMER_ERASE_ROUTING_KEY = "customer.deleted";

  public static final String ACCOUNT_STATUS_QUEUE = "account_status_queue";
  public static final String ACCOUNT_STATUS_ROUTING_KEY =
          "account.status.requested";

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }

  @Bean
  public Queue customerAccountQueue() {
    return new Queue(ACCOUNT_CUSTOMER_QUEUE, true);
  }

  @Bean
  public Queue eraseCustomerQueue() {
    return new Queue(CUSTOMER_ERASE_QUEUE, true);
  }

  @Bean
  public Queue accountStatusQueue() {
    return new Queue(ACCOUNT_STATUS_QUEUE, true);
  }

  @Bean
  public Binding bindingAccountCustomer() {
    return BindingBuilder.bind(customerAccountQueue())
            .to(exchange())
            .with(ACCOUNT_CUSTOMER_ROUTING_KEY);
  }

  @Bean
  public Binding bindingEraseCustomer() {
    return BindingBuilder.bind(eraseCustomerQueue())
            .to(exchange())
            .with(CUSTOMER_ERASE_ROUTING_KEY);
  }

  @Bean
  public Binding bindingAccountStatus() {
    return BindingBuilder.bind(accountStatusQueue())
            .to(exchange())
            .with(ACCOUNT_STATUS_ROUTING_KEY);
  }
}
