package com.ntt.customers.application.port.out;

import com.ntt.customers.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
  Flux<Customer> findAll();
  Mono<Customer> findByIdNumber(String idNumber);
  Mono<Void> save(Customer customer);
  Mono<Void> update(Customer customer);
  Mono<Void> delete(String idNumber);
}
