package com.ntt.customers.application.service;

import com.ntt.customers.application.port.in.CustomerCreateUseCase;
import com.ntt.customers.application.port.in.CustomerDeleteUseCase;
import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.application.port.in.CustomerUpdateUseCase;
import com.ntt.customers.application.port.out.CustomerRepositoryPort;
import com.ntt.customers.application.port.out.CustomerStatusAccountPort;
import com.ntt.customers.domain.exception.CustomerExistsException;
import com.ntt.customers.domain.exception.CustomerNotFoundException;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService
    implements CustomerSearchUseCase,
        CustomerCreateUseCase,
        CustomerUpdateUseCase,
        CustomerDeleteUseCase {

  private final CustomerRepositoryPort customerRepositortyPort;
  private final CustomerStatusAccountPort customerStatusAccountPort;

  @Override
  public Flux<Customer> findAll() {
    return customerRepositortyPort.findAll();
  }

  @Override
  public Mono<Long> sendCustomerRef(Long idNumber) {
    return customerRepositortyPort
        .findByIdNumber(idNumber)
        .map(Customer::getIdNumber)
        .switchIfEmpty(Mono.defer(() -> {
          log.warn("To receive the idNumber from customer, did not found {}", idNumber);
          return Mono.just(0L);
        }));
  }

  @Override
  public Flux<StatusAccountReceive> requestStatusAccount(
      LocalDate startDate, LocalDate endDate, Long idNumber
  ) {
    return customerRepositortyPort
        .findByIdNumber(idNumber)
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
        .flatMapMany(
            cus ->
                customerStatusAccountPort.requestStatusAccount(
                    startDate, endDate, idNumber, cus));
  }

  @Override
  public Mono<Void> save(Customer customer) {
    return customerRepositortyPort
        .findByIdNumber(customer.getIdNumber())
        .flatMap(
            existingCustomer ->
                Mono.<Customer>error(new CustomerExistsException("ERROR: Cliente ya existe")))
        .switchIfEmpty(
            Mono.defer(
                () -> customerRepositortyPort.save(customer.toBuilder().status(true).build())))
        .then();
  }

  @Override
  public Mono<Void> update(Customer customer) {
    return customerRepositortyPort
        .findByIdNumber(customer.getIdNumber())
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
        .flatMap(customerRepositortyPort::update);
  }

  @Override
  public Mono<Void> delete(Long idNumber) {
    return customerRepositortyPort
        .findByIdNumber(idNumber)
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
        .flatMap(it -> customerRepositortyPort.delete(idNumber));
  }
}
