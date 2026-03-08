package com.ntt.customers.infrastructure.out.persistence;

import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.infrastructure.out.persistence.entity.CustomerEntity;
import com.ntt.customers.infrastructure.out.persistence.mapper.CustomerPersistenceMapper;
import com.ntt.customers.infrastructure.out.persistence.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerPersistenceAdapterTest {

  private final Customer customer = Customer.builder()
          .id(1L)
          .name("Victor")
          .gender("Masculino")
          .age(20)
          .idNumber("1725082786")
          .address("Call Segovia y Raices")
          .phone("0984565509")
          .password("victorContrasena")
          .status(true)
          .build();
  private final CustomerEntity customerEntity = CustomerEntity.builder()
          .name("Victor")
          .gender("Masculino")
          .age(20)
          .idNumber("1725082786")
          .address("Call Segovia y Raices")
          .phone("0984565509")
          .password("victorContrasena")
          .status(true)
          .build();
  @Mock
  private CustomerRepository customerRepository;
  @Mock
  private CustomerPersistenceMapper customerPersistenceMapper;
  @InjectMocks
  private CustomerRepositoryPersistenceAdapter clientPersistenceAdapter;

  @Test
  void findAll_shouldReturnClientList() {
    var entityList = List.of(customerEntity);
    var clientList = List.of(customer);
    when(customerRepository.findAll()).thenReturn(entityList);
    when(customerPersistenceMapper.toClientList(entityList)).thenReturn(
            clientList
    );
    var result = clientPersistenceAdapter.findAll();
    assertEquals(clientList, result);
  }

  @Test
  void findByIdNumber_shouldReturnClient() {
    when(customerRepository.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customerEntity)
    );
    when(customerPersistenceMapper.toClient(customerEntity)).thenReturn(
            customer
    );
    var result = clientPersistenceAdapter.findByIdNumber(
            "1725082786"
    );
    assertTrue(result.isPresent());
    assertEquals(customer, result.get());
  }

  @Test
  void findByIdNumber_shouldReturnEmpty() {
    when(customerRepository.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    var result = clientPersistenceAdapter.findByIdNumber(
            "1725082786"
    );
    assertTrue(result.isEmpty());
  }

  @Test
  void save_shouldCallRepositorySave() {
    var entity = new Object();
    when(customerPersistenceMapper.toClientEntity(customer)).thenReturn(
            customerEntity
    );
    clientPersistenceAdapter.save(customer);
    verify(customerRepository).save(customerEntity);
  }

  @Test
  void update_shouldUpdateAndSaveIfFound() {
    when(customerRepository.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customerEntity)
    );
    doNothing()
            .when(customerPersistenceMapper)
            .update(customer, customerEntity);
    clientPersistenceAdapter.update(customer);
    verify(customerPersistenceMapper).update(customer, customerEntity);
    verify(customerRepository).save(customerEntity);
  }

  @Test
  void update_shouldDoNothingIfNotFound() {
    when(customerRepository.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    clientPersistenceAdapter.update(customer);
    verify(customerPersistenceMapper, never()).update(any(), any());
    verify(customerRepository, never()).save(any());
  }

  @Test
  void delete_shouldCallRepositoryDelete() {
    clientPersistenceAdapter.delete("123");
    verify(customerRepository).deleteByIdNumber("123");
  }
}
