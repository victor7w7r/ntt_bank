package com.ntt.customers.application.port.out;

import com.ntt.customers.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepositoryPort {
  Flux<Customer> findAll();
  Mono<Customer> findByIdNumber(Long idNumber);
  Mono<Customer> save(Customer customer);
  Mono<Void> update(Customer customer);
  Mono<Void> delete(Long idNumber);
}
