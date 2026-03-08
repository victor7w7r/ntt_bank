package com.ntt.transactions.movement.application.port.in;

import com.ntt.transactions.movement.domain.model.Movement;
import reactor.core.publisher.Mono;

public interface MovementUpdateUseCase {
  Mono<Void> update(Movement movement);
}
