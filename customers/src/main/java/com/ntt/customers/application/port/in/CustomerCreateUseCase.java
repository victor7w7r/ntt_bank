package com.ntt.customers.application.port.in;

import com.ntt.customers.domain.model.Customer;
import reactor.core.publisher.Mono;

public interface CustomerCreateUseCase {
  Mono<Void> save(Customer customer);
}
