package com.ntt.transactions.movement.infrastructure.out.persistence.repository;

import com.ntt.transactions.movement.infrastructure.out.persistence.entity.MovementEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementRepository extends ReactiveCrudRepository<MovementEntity, Long> {
  @Query("SELECT * FROM movement WHERE account_movement = :accountId")
  Flux<MovementEntity> findByAccountMovement(Long accountId);
  Mono<MovementEntity> findByUuid(String uuid);
  Mono<Void> deleteByUuid(String uuid);
}
