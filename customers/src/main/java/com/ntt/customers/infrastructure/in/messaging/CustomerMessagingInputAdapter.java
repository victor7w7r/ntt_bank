package com.ntt.customers.infrastructure.in.messaging;

import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerMessagingInputAdapter {

  private final CustomerSearchUseCase customerSearchUseCase;

  @RabbitListener(queues = "account_customer_queue")
  public Mono<Long> sendCustomerRef(String idNumber) {
    return customerSearchUseCase.sendCustomerRef(idNumber);
  }
}
