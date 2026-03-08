package com.ntt.transactions.movement.application.port.in;

import com.ntt.transactions.movement.domain.model.Movement;
import reactor.core.publisher.Flux;

public interface MovementSearchUseCase {
  Flux<Movement> findAll();
}
