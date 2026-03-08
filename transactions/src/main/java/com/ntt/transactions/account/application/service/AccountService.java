package com.ntt.transactions.account.application.service;

import com.ntt.transactions.account.application.port.in.AccountCreateUseCase;
import com.ntt.transactions.account.application.port.in.AccountDeleteUseCase;
import com.ntt.transactions.account.application.port.in.AccountSearchUseCase;
import com.ntt.transactions.account.application.port.in.AccountUpdateUseCase;
import com.ntt.transactions.account.application.port.out.AccountMessagingPort;
import com.ntt.transactions.account.application.port.out.AccountRepositoryPort;
import com.ntt.transactions.account.domain.model.Account;
import com.ntt.transactions.account.domain.model.StatusAccountReceive;
import com.ntt.transactions.account.domain.model.StatusAccountSend;
import com.ntt.transactions.common.exception.AccountExistsException;
import com.ntt.transactions.common.exception.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
@RequiredArgsConstructor
public class AccountService
    implements AccountSearchUseCase,
        AccountCreateUseCase,
        AccountUpdateUseCase,
        AccountDeleteUseCase {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final AccountRepositoryPort accountRepositoryPort;
  private final AccountMessagingPort accountMessagingPort;

  @Override
  public Flux<Account> findAll() {
    return accountRepositoryPort.findAll();
  }

  @Override
  public Mono<Void> update(Account account) {
    return accountRepositoryPort
        .findByAccountNum(account.getNumAccount())
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Cuenta no encontrada")))
        .flatMap(accountRepositoryPort::update);
  }

  @Override
  public Mono<Void> delete(Long numAccount) {
    return accountRepositoryPort
        .findByAccountNum(numAccount)
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Cliente no encontrado")))
        .flatMap(it -> accountRepositoryPort.deleteByNumAccount(numAccount));
  }

  @Override
  public Mono<Void> save(Account account, String idNumber) {
    return accountRepositoryPort
        .findByAccountNum(account.getNumAccount())
        .flatMap(existing -> Mono.<Long>error(new AccountExistsException("ERROR: Cuenta ya existe")))
        .switchIfEmpty(accountMessagingPort.sendIdReceiveRef(idNumber))
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Cliente no encontrado")))
        .flatMap(customerRef -> {
          if (account.getStatus() == null) account.setStatus(true);
          return accountRepositoryPort.save(account, customerRef);
        })
        .then();
  }

  @Override
  public Mono<Long> deleteByCustomerRef(Long customerRef) {
    return accountRepositoryPort
        .findAllByCustomerRef(customerRef)
        .hasElements()
        .flatMap(
            exists -> {
              if (exists) {
                return accountRepositoryPort.deleteByCustomerRef(customerRef).then(Mono.just(0L));
              }
              return Mono.just(0L);
            });
  }


  @Override
  public Flux<StatusAccountReceive> requestStatusAccount(StatusAccountSend req) {
    return accountRepositoryPort
        .findAllByCustomerRef(req.getCustomerRef())
        .flatMap(
            account ->
                Flux.fromIterable(account.getMovementEntities())
                    .filter(
                        movement -> {
                          final var date = movement.getDate();
                          return !date.isBefore(req.getStartDate())
                              && !date.isAfter(req.getEndDate());
                        })
                    .map(
                        movement -> {
                          var initialBalance = movement.getBalance();
                          var balanceDiff = initialBalance.add(movement.getValue());

                          return StatusAccountReceive.builder()
                              .date(movement.getDate().format(FORMATTER))
                              .customer(req.getNameCustomer())
                              .numAccount(account.getNumAccount())
                              .accountType(account.getAccountType())
                              .typeMovement(movement.getTypeMovement())
                              .movementQuantity(movement.getValue())
                              .balance(movement.getBalance())
                              .actualBalance(balanceDiff)
                              .build();
                        }));
  }
}
