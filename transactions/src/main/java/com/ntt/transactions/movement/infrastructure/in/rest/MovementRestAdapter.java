package com.ntt.transactions.movement.infrastructure.in.rest;

import com.ntt.transactions.movement.application.port.in.MovementCreateUseCase;
import com.ntt.transactions.movement.application.port.in.MovementDeleteUseCase;
import com.ntt.transactions.movement.application.port.in.MovementSearchUseCase;
import com.ntt.transactions.movement.application.port.in.MovementUpdateUseCase;
import com.ntt.transactions.movement.infrastructure.in.rest.mapper.MovementRestMapper;
import com.ntt.transactions.movement.infrastructure.in.rest.model.MovementOperationResponse;
import com.ntt.transactions.movement.infrastructure.in.rest.model.MovementRequest;
import com.ntt.transactions.movement.infrastructure.in.rest.model.MovementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin("*")
@RestController
@Slf4j
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementRestAdapter {

  private final MovementRestMapper movementRestMapper;
  private final MovementSearchUseCase movementSearchUseCase;
  private final MovementCreateUseCase movementCreateUseCase;
  private final MovementUpdateUseCase movementUpdateUseCase;
  private final MovementDeleteUseCase movementDeleteUseCase;

  @GetMapping
  private Flux<MovementResponse> findAll() {
    return movementSearchUseCase
        .findAll()
        .doFirst(() -> log.info("Querying all movements"))
        .map(movementRestMapper::toMovementResponse);
  }

  @PostMapping("{numAccount}")
  private Mono<ResponseEntity<?>> saveMovement(
      @RequestBody @Valid MovementRequest request, @PathVariable Long numAccount) {
    return Mono.just(request)
        .doFirst(() -> log.info("Creating movement with num account {}", numAccount))
        .map(movementRestMapper::toMovement)
        .flatMap((movement) -> movementCreateUseCase.save(movement, numAccount))
        .then(
            Mono.fromCallable(
                () ->
                    ResponseEntity.status(201)
                        .body(
                            MovementOperationResponse.builder()
                                .message("Movimiento guardado exitosamente")
                                .build())));
  }

  @PutMapping
  private Mono<ResponseEntity<?>> updateMovement(@RequestBody @Valid MovementRequest request) {
    return Mono.just(request)
        .doFirst(() -> log.info("Updating movement {}", request.getUuid()))
        .map(movementRestMapper::toMovement)
        .flatMap(movementUpdateUseCase::update)
        .then(
            Mono.fromCallable(
                () ->
                    ResponseEntity.status(201)
                        .body(
                            MovementOperationResponse.builder()
                                .message("Movimiento actualizado exitosamente")
                                .build())));
  }

  @DeleteMapping("{uuid}")
  private Mono<ResponseEntity<?>> deleteMovement(@PathVariable String uuid) {
    return movementDeleteUseCase
        .delete(uuid)
        .doFirst(() -> log.info("Deleting movement {}", uuid))
        .then(
            Mono.fromCallable(
                () ->
                    ResponseEntity.status(202)
                        .body(
                            MovementOperationResponse.builder()
                                .message("Movimiento eliminado exitosamente")
                                .build())));
  }
}
