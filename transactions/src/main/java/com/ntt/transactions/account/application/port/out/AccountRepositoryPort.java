package com.ntt.transactions.account.application.port.out;

import com.ntt.transactions.account.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepositoryPort {
  Flux<Account> findAll();

  Flux<Account> findAllByCustomerRef(Long customerRef);

  Mono<Account> findByAccountNum(Long accountNum);

  Mono<Void> save(Account account, Long customerRef);

  Mono<Void> saveOnly(Account account);

  Mono<Void> update(Account account);

  Mono<Void> deleteByNumAccount(Long numAccount);

  Mono<Void> deleteByCustomerRef(Long customerRef);
}
