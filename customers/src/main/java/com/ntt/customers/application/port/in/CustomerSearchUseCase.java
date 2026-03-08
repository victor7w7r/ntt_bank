package com.ntt.customers.application.port.in;

import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import java.time.LocalDate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerSearchUseCase {
  Flux<StatusAccountReceive> requestStatusAccount(
      LocalDate startDate, LocalDate endDate, String idNumber);

  Mono<Long> sendCustomerRef(String idNumber);

  Flux<Customer> findAll();
}
