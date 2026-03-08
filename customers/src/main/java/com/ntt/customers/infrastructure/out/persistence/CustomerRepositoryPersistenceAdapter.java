package com.ntt.customers.infrastructure.out.persistence;

import com.ntt.customers.application.port.out.CustomerRepositoryPort;
import com.ntt.customers.domain.exception.CustomerNotFoundException;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.infrastructure.out.persistence.mapper.CustomerPersistenceMapper;
import com.ntt.customers.infrastructure.out.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerRepositoryPersistenceAdapter implements CustomerRepositoryPort {

  private final CustomerRepository customerRepository;
  private final CustomerPersistenceMapper customerPersistenceMapper;

  @Override
  public Flux<Customer> findAll() {
    return customerRepository.findAll().map(customerPersistenceMapper::toCustomer);
  }

  @Override
  public Mono<Customer> findByIdNumber(String idNumber) {
    return customerRepository.findByIdNumber(idNumber).map(customerPersistenceMapper::toCustomer);
  }

  @Override
  public Mono<Customer> save(Customer customer) {
    return Mono.just(customer)
        .map(customerPersistenceMapper::toCustomerEntity)
        .flatMap(customerRepository::save)
        .map(cust -> customer);
  }

  @Override
  public Mono<Void> update(Customer customer) {
    return customerRepository
        .findByIdNumber(customer.getIdNumber())
            .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
            .flatMap(
            customerEntity -> {
              customerPersistenceMapper.update(customer, customerEntity);
              log.info("Updated Entity {}", customerEntity);
              return customerRepository.save(customerEntity);
            })
        .then();
  }

  @Override
  public Mono<Void> delete(String idNumber) {
    return customerRepository
        .findByIdNumber(idNumber)
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
        .flatMap(customerRepository::delete)
        .then();
  }
}
