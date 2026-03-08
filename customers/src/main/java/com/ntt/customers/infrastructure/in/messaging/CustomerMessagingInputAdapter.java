package com.ntt.customers.infrastructure.in.messaging;

import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CustomerMessagingInputAdapter {

  private final CustomerSearchUseCase customerSearchUseCase;

  @Bean
  public Function<Flux<String>, Flux<Long>> customerRefProcessor() {
    return flux ->
            flux.flatMap(customerSearchUseCase::sendCustomerRef);
  }
}
