package com.ntt.transactions.movement.application.service;

import com.ntt.transactions.account.application.port.out.AccountRepositoryPort;
import com.ntt.transactions.common.exception.AccountExistsException;
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

  @Override
  public Flux<Movement> findAll() {
    return movementRepositoryPort.findAll();
  }

  @Override
  public Mono<Void> save(Movement movement, Long numAccount) {
    return movementRepositoryPort
        .findByUuid(movement.getUuid())
        .flatMap(
            existing -> Mono.<Void>error(new EntityNotFoundException("ERROR: Movimiento ya existe")))
        .switchIfEmpty(Mono.defer(() -> accountRepositoryPort.findByAccountNum(numAccount)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: Cuenta no encontrada")))
                .flatMap(accountFound -> {
                  final var movementFunds = movement.getValue();
                  final var balanceDiff = accountFound.getInitialFunds().add(movementFunds);
                  if (balanceDiff.compareTo(BigDecimal.ZERO) < 0) {
                    return Mono.error(new InsufficientFundsException("ERROR: Saldo insuficiente"));
                  }
                  if (movement.getUuid() == null) movement.setUuid(java.util.UUID.randomUUID().toString());
                  if (movement.getDate() == null) movement.setDate(java.time.LocalDate.now());

                  movement.setTypeMovement(movementFunds.signum() > 0 ? "Deposito" : "Retiro");
                  accountFound.setInitialFunds(balanceDiff);

                  return accountRepositoryPort.saveOnly(accountFound)
                          .then(Mono.fromCallable(() ->
                                  Movement.builder()
                                          .date(movement.getDate())
                                          .typeMovement(movement.getTypeMovement())
                                          .value(movementFunds)
                                          .balance(balanceDiff)
                                          .uuid(movement.getUuid())
                                          .accountMovement(accountFound.getId().toString())
                                          .build()
                          ))
                          .flatMap(movementRepositoryPort::save);
                })
        ));
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
