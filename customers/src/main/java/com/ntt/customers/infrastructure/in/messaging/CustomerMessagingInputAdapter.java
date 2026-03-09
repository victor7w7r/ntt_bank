package com.ntt.customers.infrastructure.in.messaging;

import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.infrastructure.config.BrokerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerMessagingInputAdapter {

  private final CustomerSearchUseCase customerSearchUseCase;

  @RabbitListener(queues = BrokerConfig.ACCOUNT_STATUS_QUEUE)
  public Long sendCustomerRef(Long idNumber) {
    log.info("idNumber recibido: {}", idNumber);
    final var number = customerSearchUseCase.sendCustomerRef(idNumber).block();
    log.info("number resultante {}", number);
    return number;
  }
}