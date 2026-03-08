package com.ntt.transactions.movement.application.port.out;

import com.ntt.transactions.movement.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementRepositoryPort {
  Flux<Movement> findAll();

  Mono<Movement> findByUuid(String uuid);

  Mono<Void> save(Movement movement);

  Mono<Void> update(Movement movement);

  Mono<Void> delete(String uuid);
}
