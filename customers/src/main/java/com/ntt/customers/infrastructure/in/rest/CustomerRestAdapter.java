package com.ntt.customers.infrastructure.in.rest;

import com.ntt.customers.application.port.in.CustomerCreateUseCase;
import com.ntt.customers.application.port.in.CustomerDeleteUseCase;
import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.application.port.in.CustomerUpdateUseCase;
import com.ntt.customers.infrastructure.in.rest.mapper.CustomerRestMapper;
import com.ntt.customers.infrastructure.in.rest.model.CustomerOperationResponse;
import com.ntt.customers.infrastructure.in.rest.model.CustomerRequest;
import com.ntt.customers.infrastructure.in.rest.model.CustomerResponse;
import com.ntt.customers.infrastructure.in.rest.model.StatusAccountReceiveRes;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerRestAdapter {

  private final CustomerSearchUseCase customerSearchUseCase;
  private final CustomerCreateUseCase customerCreateUseCase;
  private final CustomerUpdateUseCase customerUpdateUseCase;
  private final CustomerDeleteUseCase customerDeleteUseCase;
  private final CustomerRestMapper customerRestMapper;

  @GetMapping
  public Flux<CustomerResponse> getAllCustomers() {
    return customerSearchUseCase
            .findAll()
            .doFirst(() -> log.info("Querying all customers"))
            .map(customerRestMapper::toCustomerResponse);
  }

  @GetMapping("reports")
  public Flux<StatusAccountReceiveRes> getAccountStatus(
          @RequestParam LocalDate startDate,
          @RequestParam LocalDate endDate,
          @RequestParam String idNumber
  ) {
    return customerSearchUseCase
            .requestStatusAccount(startDate, endDate, idNumber)
            .doFirst(() -> log.info("Querying status account for customer {}", idNumber))
            .map(customerRestMapper::toStatusAccountReceiveRes);
  }

  @PostMapping
  public Mono<ResponseEntity<?>> saveCustomer(
          @Valid @RequestBody CustomerRequest request
  ) {
    return Mono.just(request)
            .doFirst(() -> log.info("Creating customer {}", request.getName()))
            .map(customerRestMapper::toCustomer)
            .flatMap(customerCreateUseCase::save)
            .then(Mono.fromCallable(() ->
                    ResponseEntity.status(201).body(
                            CustomerOperationResponse.builder()
                                    .status("status")
                                    .message("Cliente guardado exitosamente")
                                    .build()
                    )
            ));
  }

  @PutMapping
  public Mono<ResponseEntity<?>> updateCustomer(
          @Valid @RequestBody CustomerRequest request
  ) {
    return Mono.just(request)
            .doFirst(() -> log.info("Updating customer {}", request.getName()))
            .map(customerRestMapper::toCustomer)
            .flatMap(customerUpdateUseCase::update)
            .then(Mono.fromCallable(() ->
                    ResponseEntity.status(201).body(
                            CustomerOperationResponse.builder()
                                    .status("status")
                                    .message("Cliente actualizado exitosamente")
                                    .build()
                    )
            ));
  }

  @DeleteMapping("{id}")
  public Mono<ResponseEntity<?>> deleteCustomer(@PathVariable String id) {
    return customerDeleteUseCase.delete(id)
            .doFirst(() -> log.info("Deleting customer {}", id))
            .then(Mono.fromCallable(() ->
                    ResponseEntity.status(202).body(
                            CustomerOperationResponse.builder()
                                    .status("status")
                                    .message("Cliente eliminado exitosamente")
                                    .build()
                    )
            ));
  }
}
