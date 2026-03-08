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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerSearchUseCase, CustomerCreateUseCase, CustomerUpdateUseCase, CustomerDeleteUseCase {

  private final CustomerRepositoryPort customerRepositortyPort;
  private final CustomerStatusAccountPort customerStatusAccountPort;

  @Override
  public Flux<Customer> findAll() {
    return customerRepositortyPort.findAll();
  }

  @Override
  public Mono<Long> sendCustomerRef(String idNumber) {
    return customerRepositortyPort
            .findByIdNumber(idNumber)
            .map(Customer::getId)
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")));
  }

  @Override
  public Flux<StatusAccountReceive> requestStatusAccount(
          LocalDate startDate,
          LocalDate endDate,
          String idNumber
  ) {
   return customerRepositortyPort
            .findByIdNumber(idNumber)
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
            .flatMapMany(customerFound -> customerStatusAccountPort.emitRequestStatusAccount(
                    startDate,
                    endDate,
                    idNumber,
                    customerFound
            ));
  }

  @Override
  public Mono<Void> save(Customer customer) {
    return customerRepositortyPort
          .findByIdNumber(customer.getIdNumber())
            .filter( it -> it.getId() != null )
            .switchIfEmpty(Mono.error(new CustomerExistsException("ERROR: Cliente ya existe")))
            .doOnNext(it -> it.setStatus(true))
            .flatMap(customerRepositortyPort::save);
  }

  @Override
  public Mono<Void> update(Customer customer) {
   return customerRepositortyPort
            .findByIdNumber(customer.getIdNumber())
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
            .flatMap(customerRepositortyPort::update);
  }

  @Override
  public Mono<Void> delete(String idNumber) {
    return customerRepositortyPort
            .findByIdNumber(idNumber)
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
            .flatMap(it -> customerRepositortyPort.delete(idNumber));
  }
}
