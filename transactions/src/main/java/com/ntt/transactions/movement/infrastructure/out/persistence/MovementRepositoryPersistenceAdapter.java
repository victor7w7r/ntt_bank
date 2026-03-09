package com.ntt.transactions.movement.infrastructure.out.persistence;

import com.ntt.transactions.common.exception.MovementNotFoundException;
import com.ntt.transactions.movement.application.port.out.MovementRepositoryPort;
import com.ntt.transactions.movement.domain.model.Movement;
import com.ntt.transactions.movement.infrastructure.out.persistence.mapper.MovementPersistenceMapper;
import com.ntt.transactions.movement.infrastructure.out.persistence.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MovementRepositoryPersistenceAdapter implements MovementRepositoryPort {

  private final MovementRepository movementRepository;
  private final MovementPersistenceMapper movementPersistenceMapper;

  @Override
  public Flux<Movement> findAll() {
    return movementRepository
            .findAll()
            .map(movementPersistenceMapper::toMovement);
  }

 @Override
  public Flux<Movement> findByAccountMovement(Long accountMovement) {
    return movementRepository
            .findByAccountMovement(accountMovement)
            .map(movementPersistenceMapper::toMovement);
  }

  @Override
  public Mono<Movement> findByUuid(String uuid) {
    return movementRepository
            .findByUuid(uuid)
            .map(movementPersistenceMapper::toMovement);
  }

  @Override
  public Mono<Void> save(Movement movement) {
    return Mono.just(movement)
            .map(movementPersistenceMapper::toMovementEntity)
            .flatMap(movementRepository::save)
            .then();
  }

  @Override
  public Mono<Void> update(Movement movement) {
    return movementRepository.findByUuid(movement.getUuid())
            .flatMap(movementEntity -> {
              movementPersistenceMapper.update(movement, movementEntity);
              return movementRepository.save(movementEntity);
            })
            .switchIfEmpty(Mono.error(new MovementNotFoundException("ERROR: Movimiento no encontrado")))
            .then();

  }

  @Override
  public Mono<Void> delete(String uuid) {
    return movementRepository.findByUuid(uuid)
            .flatMap(movementEntity ->
                    movementRepository.deleteByUuid(movementEntity.getUuid())
            )
            .switchIfEmpty(Mono.error(new MovementNotFoundException("ERROR: Movimiento no encontrado")))
            .then();
  }
}
