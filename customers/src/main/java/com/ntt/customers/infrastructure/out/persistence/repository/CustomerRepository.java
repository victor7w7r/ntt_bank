package com.ntt.customers.infrastructure.out.persistence.repository;

import com.ntt.customers.infrastructure.out.persistence.entity.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
  Mono<CustomerEntity> findByIdNumber(String idNumber);
  Mono<Void> deleteByIdNumber(String idNumber);
}
