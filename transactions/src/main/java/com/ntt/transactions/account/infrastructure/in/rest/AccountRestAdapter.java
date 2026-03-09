package com.ntt.transactions.account.infrastructure.in.rest;

import com.ntt.transactions.account.application.port.in.AccountCreateUseCase;
import com.ntt.transactions.account.application.port.in.AccountDeleteUseCase;
import com.ntt.transactions.account.application.port.in.AccountSearchUseCase;
import com.ntt.transactions.account.application.port.in.AccountUpdateUseCase;
import com.ntt.transactions.account.infrastructure.in.rest.mapper.AccountRestMapper;
import com.ntt.transactions.account.infrastructure.in.rest.model.AccountOperationResponse;
import com.ntt.transactions.account.infrastructure.in.rest.model.AccountRequest;
import com.ntt.transactions.account.infrastructure.in.rest.model.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountRestAdapter {

  private final AccountSearchUseCase accountSearchUseCase;
  private final AccountCreateUseCase accountCreateUseCase;
  private final AccountUpdateUseCase accountUpdateUseCase;
  private final AccountDeleteUseCase accountDeleteUseCase;
  private final AccountRestMapper accountRestMapper;

  @GetMapping
  private Flux<AccountResponse> findAll() {
    return accountSearchUseCase
            .findAll()
            .doFirst(() -> log.info("Querying all accounts"))
            .map(accountRestMapper::toAccountResponse);
  }

  @PostMapping("{idNumber}")
  private Mono<ResponseEntity<?>> saveAccount(
          @Valid @RequestBody AccountRequest request,
          @PathVariable Long idNumber
  ) {
    return Mono.just(request)
            .doFirst(() -> log.info("Creating account {}", request.getNumAccount()))
            .map(accountRestMapper::toAccount)
            .flatMap(account -> accountCreateUseCase.save(account, idNumber))
            .then(Mono.fromCallable(() ->
                    ResponseEntity.status(201).body(
                            AccountOperationResponse.builder()
                                    .message("Cuenta guardada exitosamente")
                                    .build()
                    )
            ));
  }

  @PutMapping
  private Mono<ResponseEntity<?>> updateAccount(
          @Valid @RequestBody AccountRequest request
  ) {
    return Mono.just(request)
            .doFirst(() -> log.info("Updating account {}", request.getNumAccount()))
            .map(accountRestMapper::toAccount)
            .flatMap(accountUpdateUseCase::update)
            .then(Mono.fromCallable(() ->
                    ResponseEntity.status(201).body(
                            AccountOperationResponse.builder()
                                    .message("Cuenta actualizada exitosamente")
                                    .build()
                    )
            ));
  }

  @DeleteMapping("{numAccount}")
  private Mono<ResponseEntity<?>> deleteAccount(
          @PathVariable Long numAccount
  ) {
    return accountDeleteUseCase.delete(numAccount)
            .doFirst(() -> log.info("Deleting account {}", numAccount))
            .then(Mono.fromCallable(() ->
                    ResponseEntity.status(202).body(
                            AccountOperationResponse.builder()
                                    .message("Cuenta eliminada exitosamente")
                                    .build()
                    )
            ));

  }
}
