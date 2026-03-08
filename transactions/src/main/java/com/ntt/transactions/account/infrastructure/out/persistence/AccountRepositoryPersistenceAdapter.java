package com.ntt.transactions.account.infrastructure.out.persistence;

import com.ntt.transactions.account.application.port.out.AccountRepositoryPort;
import com.ntt.transactions.account.domain.model.Account;
import com.ntt.transactions.account.infrastructure.out.persistence.mapper.AccountPersistenceMapper;
import com.ntt.transactions.account.infrastructure.out.persistence.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountRepositoryPersistenceAdapter implements AccountRepositoryPort {

  private final AccountRepository accountRepository;
  private final AccountPersistenceMapper accountPersistenceMapper;

  @Override
  public Flux<Account> findAll() {
    return accountRepository.findAll().map(accountPersistenceMapper::toAccount);
  }

  @Override
  public Flux<Account> findAllByCustomerRef(Long customerRef) {
    return accountRepository
        .findAllByCustomerRef(customerRef)
        .map(accountPersistenceMapper::toAccount);
  }

  @Override
  public Mono<Account> findByAccountNum(Long accountNum) {
    return accountRepository.findByNumAccount(accountNum).map(accountPersistenceMapper::toAccount);
  }

  @Override
  public Mono<Void> save(Account account, Long customerRef) {
    return Mono.just(account)
        .map(accountPersistenceMapper::toAccountEntity)
        .flatMap(accountRepository::save)
        .then();
  }

  @Override
  public Mono<Void> saveOnly(Account account) {
    return accountRepository
        .findByNumAccount(account.getNumAccount())
        .flatMap(
            accountEntity -> {
              accountPersistenceMapper.updateWithoutNumAccount(account, accountEntity);
              return accountRepository.save(accountEntity).then();
            });
  }

  @Override
  public Mono<Void> update(Account account) {
    return accountRepository
        .findByNumAccount(account.getNumAccount())
        .flatMap(
            accountEntity -> {
              accountPersistenceMapper.update(account, accountEntity);
              return accountRepository.save(accountEntity).then();
            });
  }

  @Override
  public Mono<Void> deleteByNumAccount(Long numAccount) {
    return accountRepository.deleteByNumAccount(numAccount).then();
  }

  @Override
  public Mono<Void> deleteByCustomerRef(Long customerRef) {
    return accountRepository.deleteByCustomerRef(customerRef).then();
  }
}
