package com.ntt.transactions.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
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
  public static final String ACCOUNT_STATUS_ROUTING_KEY = "account.status.requested";

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }

  @Bean
  public MessageConverter simpleConverter() {
    return new SimpleMessageConverter();
  }

  @Bean
  public Queue customerAccountQueue() {
    return new Queue(ACCOUNT_CUSTOMER_QUEUE, true);
  }

  @Bean
  public Queue borrarClientQueue() {
    return new Queue(CUSTOMER_ERASE_QUEUE, true);
  }

  @Bean
  public Queue accountStatusQueue() {
    return new Queue(ACCOUNT_STATUS_QUEUE, true);
  }

  @Bean
  public Binding bindingAccountClient() {
    return BindingBuilder.bind(customerAccountQueue())
        .to(exchange())
        .with(ACCOUNT_CUSTOMER_ROUTING_KEY);
  }

  @Bean
  public Binding bindingEraseClient() {
    return BindingBuilder.bind(borrarClientQueue()).to(exchange()).with(CUSTOMER_ERASE_ROUTING_KEY);
  }

  @Bean
  public Binding bindingAccountStatus() {
    return BindingBuilder.bind(accountStatusQueue())
        .to(exchange())
        .with(ACCOUNT_STATUS_ROUTING_KEY);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    final var template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(simpleConverter());
    template.setMandatory(true);
    template.setReplyTimeout(5000);
    return template;
  }
}
