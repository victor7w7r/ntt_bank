package com.ntt.customers.infrastructure.out.persistence.repository;

import com.ntt.customers.infrastructure.out.persistence.entity.CustomerEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<CustomerEntity, Long> {
  @Query("SELECT * FROM customer WHERE id_number = :idNumber")
  Mono<CustomerEntity> findByIdNumber(Long idNumber);
}
