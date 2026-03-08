package com.ntt.transactions.account.infrastructure.out.persistence.repository;

import com.ntt.transactions.account.infrastructure.out.persistence.entity.AccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {
  Mono<AccountEntity> findByNumAccount(Long numAccount);
  Flux<AccountEntity> findAllByCustomerRef(Long customerRef);
  Mono<Void> deleteByCustomerRef(Long customerRef);
  Mono<Void> deleteByNumAccount(Long numAccount);
}
