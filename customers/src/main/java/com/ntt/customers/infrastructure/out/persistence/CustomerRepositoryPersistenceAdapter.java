package com.ntt.customers.infrastructure.out.persistence;

import com.ntt.customers.application.port.out.CustomerRepositoryPort;
import com.ntt.customers.domain.exception.CustomerNotFoundException;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.infrastructure.out.persistence.mapper.CustomerPersistenceMapper;
import com.ntt.customers.infrastructure.out.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
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
  public Mono<Void> save(Customer customer) {
    return Mono.just(customer)
        .map(customerPersistenceMapper::toCustomerEntity)
        .flatMap(customerRepository::save)
        .then();
  }

  @Override
  public Mono<Void> update(Customer customer) {
    return customerRepository
        .findByIdNumber(customer.getIdNumber())
        .flatMap(
            customerEntity -> {
              customerPersistenceMapper.update(customer, customerEntity);
              return customerRepository.save(customerEntity);
            })
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
        .then();
  }

  @Override
  public Mono<Void> delete(String idNumber) {
    return customerRepository
        .findByIdNumber(idNumber)
        .flatMap(
            customerEntity -> customerRepository.deleteByIdNumber(customerEntity.getIdNumber()))
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("ERROR: Cliente no encontrado")))
        .then();
  }
}
