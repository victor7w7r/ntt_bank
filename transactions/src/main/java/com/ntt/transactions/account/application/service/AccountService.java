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
import com.ntt.transactions.movement.application.port.out.MovementRepositoryPort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService
    implements AccountSearchUseCase,
        AccountCreateUseCase,
        AccountUpdateUseCase,
        AccountDeleteUseCase {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final AccountRepositoryPort accountRepositoryPort;
  private final MovementRepositoryPort movementRepositoryPort;
  private final AccountMessagingPort accountMessagingPort;

  @Override
  public Flux<Account> findAll() {
    return accountRepositoryPort
      .findAll()
      .flatMap(account ->
        movementRepositoryPort.findByAccountMovement(account.getId())
          .collectList()
          .map(movements -> {
            account.setMovements(movements);
            return account;
          })
      );
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
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Cuenta no encontrada")))
        .flatMap(it -> accountRepositoryPort.deleteByNumAccount(numAccount));
  }

  @Override
  public Mono<Void> save(Account account, Long idNumber) {
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

  //Reportes
  @Override
  public Flux<StatusAccountReceive> requestStatusAccount(StatusAccountSend req) {
    return accountRepositoryPort
            .findAllByCustomerRef(req.getCustomerRef())
            .flatMap(account -> movementRepositoryPort.findByAccountMovement(account.getId())
            .filter(movement -> {
              final var date = movement.getDate();
              return !date.isBefore(LocalDate.parse(req.getStartDate()))
                  && !date.isAfter(LocalDate.parse(req.getEndDate()));
            })
            .map(movement -> {
              var initialBalance = movement.getBalance();
              var balanceDiff = initialBalance.add(movement.getValue());
              return StatusAccountReceive.builder()
                      .date(movement.getDate().format(FORMATTER))
                      .customer(req.getCustomerName())
                      .numAccount(account.getNumAccount())
                      .accountType(account.getAccountType())
                      .movementType(movement.getTypeMovement())
                      .movement(movement.getValue())
                      .balance(movement.getBalance())
                      .availableFunds(balanceDiff)
                      .build();
            })
          );
  }
}
