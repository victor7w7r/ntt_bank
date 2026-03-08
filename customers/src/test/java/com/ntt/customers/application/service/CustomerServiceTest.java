package com.ntt.customers.application.service;

import com.ntt.customers.application.port.out.CustomerRepositoryPort;
import com.ntt.customers.application.port.out.CustomerStatusAccountPort;
import com.ntt.customers.domain.exception.CustomerNotFoundException;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

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
  @Mock
  private CustomerRepositoryPort customerRepositoryPort;
  @Mock
  private CustomerStatusAccountPort customerStatusAccountPort;
  @InjectMocks
  private CustomerService clientService;

  @Test
  void findAll_shouldReturnClients() {
    List<Customer> customers = List.of(customer);
    when(customerRepositoryPort.findAll()).thenReturn(customers);
    assertEquals(customers, clientService.findAll());
  }

  @Test
  void envioClientRef_shouldReturnId() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customer)
    );
    assertEquals(1L, clientService.sendCustomerRef("1725082786"));
  }

  @Test
  void envioClientRef_shouldThrowIfNotFound() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    assertThrows(CustomerNotFoundException.class, () ->
            clientService.sendCustomerRef("1725082786")
    );
  }

  @Test
  void requestStatusAccount_shouldReturnList() {
    LocalDate start = LocalDate.now();
    LocalDate end = LocalDate.now();
    List<StatusAccountReceive> status = List.of(
            mock(StatusAccountReceive.class)
    );
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customer)
    );
    when(
            customerStatusAccountPort.requestStatusAccount(
                    start,
                    end,
                    "1725082786",
                    customer
            )
    ).thenReturn(status);
    assertEquals(
            status,
            clientService.requestStatusAccount(start, end, "1725082786")
    );
  }

  @Test
  void requestStatusAccount_shouldThrowIfNotFound() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    assertThrows(CustomerNotFoundException.class, () ->
            clientService.requestStatusAccount(
                    LocalDate.now(),
                    LocalDate.now(),
                    "1725082786"
            )
    );
  }

  @Test
  void save_shouldSaveIfNotExists() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    clientService.save(customer);
    assertTrue(customer.getStatus());
    verify(customerRepositoryPort).save(customer);
  }

  @Test
  void save_shouldThrowIfExists() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customer)
    );
    assertThrows(CustomerNotFoundException.class, () ->
            clientService.save(customer)
    );
  }

  @Test
  void update_shouldUpdateIfExists() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customer)
    );
    clientService.update(customer);
    verify(customerRepositoryPort).update(customer);
  }

  @Test
  void update_shouldThrowIfNotFound() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    assertThrows(CustomerNotFoundException.class, () ->
            clientService.update(customer)
    );
  }

  @Test
  void delete_shouldDeleteIfExists() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.of(customer)
    );
    clientService.delete("1725082786");
    verify(customerRepositoryPort).delete("1725082786");
  }

  @Test
  void delete_shouldThrowIfNotFound() {
    when(customerRepositoryPort.findByIdNumber("1725082786")).thenReturn(
            Optional.empty()
    );
    assertThrows(CustomerNotFoundException.class, () ->
            clientService.delete("1725082786")
    );
  }
}
