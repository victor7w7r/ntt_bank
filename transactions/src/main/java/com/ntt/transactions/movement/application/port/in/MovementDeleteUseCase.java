package com.ntt.transactions.movement.application.port.in;

import reactor.core.publisher.Mono;

public interface MovementDeleteUseCase {
  Mono<Void> delete(String uuid);
}
