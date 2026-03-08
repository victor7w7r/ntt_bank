package com.ntt.transactions.movement.application.service;

import com.ntt.transactions.account.application.port.out.AccountRepositoryPort;
import com.ntt.transactions.common.exception.EntityNotFoundException;
import com.ntt.transactions.common.exception.InsufficientFundsException;
import com.ntt.transactions.movement.application.port.in.MovementCreateUseCase;
import com.ntt.transactions.movement.application.port.in.MovementDeleteUseCase;
import com.ntt.transactions.movement.application.port.in.MovementSearchUseCase;
import com.ntt.transactions.movement.application.port.in.MovementUpdateUseCase;
import com.ntt.transactions.movement.application.port.out.MovementRepositoryPort;
import com.ntt.transactions.movement.domain.model.Movement;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovementService
    implements MovementSearchUseCase,
        MovementCreateUseCase,
        MovementUpdateUseCase,
        MovementDeleteUseCase {

  private final MovementRepositoryPort movementRepositoryPort;
  private final AccountRepositoryPort accountRepositoryPort;

  public Flux<Movement> findAll() {
    return movementRepositoryPort.findAll();
  }

  private void prepareMovement(Movement m, BigDecimal funds, BigDecimal balance, String accId) {
    if (m.getUuid() == null) m.setUuid(java.util.UUID.randomUUID().toString());
    if (m.getDate() == null) m.setDate(java.time.LocalDate.now());
    m.setTypeMovement(funds.compareTo(BigDecimal.ZERO) > 0 ? "Deposito" : "Retiro");
    m.setAccountId(accId);
    m.setBalance(balance);
  }

  @Override
  public Mono<Void> save(Movement movement, Long numAccount) {
    return movementRepositoryPort
        .findByUuid(movement.getUuid())
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: El movimiento no existe")))
        .flatMap(
            foundMovement ->
                accountRepositoryPort
                    .findByAccountNum(numAccount)
                    .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("ERROR: Cuenta no encontrada")))
                    .flatMap(
                        account -> {
                          final var movementFunds = movement.getValue();
                          final var balanceDiff = account.getInitialFunds().add(movementFunds);

                          if (balanceDiff.compareTo(BigDecimal.ZERO) < 0) {
                            return Mono.error(
                                new InsufficientFundsException("ERROR: Saldo insuficiente"));
                          }
                          prepareMovement(
                              movement, movementFunds, balanceDiff, account.getId().toString());
                          account.setInitialFunds(balanceDiff);

                          return accountRepositoryPort
                              .saveOnly(account)
                              .then(movementRepositoryPort.save(movement));
                        }));
  }

  @Override
  public Mono<Void> update(Movement movement) {
    return movementRepositoryPort
        .findByUuid(movement.getUuid())
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: movimiento no encontrado")))
        .flatMap(movementRepositoryPort::update);
  }

  @Override
  public Mono<Void> delete(String uuid) {
    return movementRepositoryPort
        .findByUuid(uuid)
        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: movimiento no encontrado")))
        .flatMap(it -> movementRepositoryPort.delete(uuid));
  }
}
