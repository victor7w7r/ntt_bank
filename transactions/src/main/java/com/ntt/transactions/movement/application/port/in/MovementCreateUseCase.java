package com.ntt.transactions.movement.application.port.in;

import com.ntt.transactions.movement.domain.model.Movement;
import reactor.core.publisher.Mono;

public interface MovementCreateUseCase {
  Mono<Void> save(Movement movement, Long numAccount);
}
