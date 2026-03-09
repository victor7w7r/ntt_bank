package com.ntt.transactions.movement.service;

import com.ntt.transactions.account.application.port.out.AccountRepositoryPort;
import com.ntt.transactions.common.exception.EntityNotFoundException;
import com.ntt.transactions.common.exception.InsufficientFundsException;
import com.ntt.transactions.movement.application.port.out.MovementRepositoryPort;
import com.ntt.transactions.movement.application.service.MovementService;
import com.ntt.transactions.movement.domain.model.Movement;
import com.ntt.transactions.account.domain.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovementService Tests")
class MovementServiceTest {
  @Mock
  private MovementRepositoryPort movementRepositoryPort;

  @Mock
  private AccountRepositoryPort accountRepositoryPort;

  @InjectMocks
  private MovementService movementService;

  @BeforeEach
  void setUp() {
    movementService = new MovementService(movementRepositoryPort, accountRepositoryPort);
  }

  @Nested
  @DisplayName("findAll Tests")
  class FindAllTests {

    @Test
    @DisplayName("should return all movements when they exist")
    void shouldFindAllMovements() {
      final var movement1 = Movement.builder()
              .uuid(UUID.randomUUID().toString())
              .value(new BigDecimal("100.00"))
              .typeMovement("Deposito")
              .balance(new BigDecimal("1100.00"))
              .date(LocalDate.now())
              .accountMovement("account1")
              .build();

      final var movement2 = Movement.builder()
              .uuid(UUID.randomUUID().toString())
              .value(new BigDecimal("-50.00"))
              .typeMovement("Retiro")
              .balance(new BigDecimal("1050.00"))
              .date(LocalDate.now())
              .accountMovement("account1")
              .build();

      when(movementRepositoryPort.findAll())
              .thenReturn(Flux.just(movement1, movement2));

      StepVerifier.create(movementService.findAll())
              .expectNext(movement1)
              .expectNext(movement2)
              .verifyComplete();

      verify(movementRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("should return empty flux when no movements exist")
    void shouldReturnEmptyFluxWhenNoMovements() {
      when(movementRepositoryPort.findAll())
              .thenReturn(Flux.empty());

      StepVerifier.create(movementService.findAll())
              .expectComplete()
              .verify();

      verify(movementRepositoryPort, times(1)).findAll();
    }
  }

  @Nested
  @DisplayName("save Tests")
  class SaveTests {
    private static final Long numAccountFake = 123456L;
    private static final String movementUuidFake = "550e8400-e29b-41d4-a716-446655440000";

    @Test
    @DisplayName("should save new movement with positive value (deposit)")
    void shouldSaveDepositMovement() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("500.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).findByUuid(movementUuidFake);
      verify(accountRepositoryPort).findByAccountNum(numAccountFake);
      verify(accountRepositoryPort).saveOnly(any(Account.class));
      verify(movementRepositoryPort).save(any(Movement.class));
    }

    @Test
    @DisplayName("should save new movement with negative value (withdrawal)")
    void shouldSaveWithdrawalMovement() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("-200.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).findByUuid(movementUuidFake);
      verify(accountRepositoryPort).findByAccountNum(numAccountFake);
    }

    @Test
    @DisplayName("should fail when movement already exists")
    void shouldFailWhenMovementExists() {
      String existingUuid = movementUuidFake;
      final var existingMovement = Movement.builder()
              .uuid(existingUuid)
              .value(new BigDecimal("100.00"))
              .build();

      final var newMovement = Movement.builder()
              .uuid(existingUuid)
              .value(new BigDecimal("200.00"))
              .build();

      when(movementRepositoryPort.findByUuid(existingUuid))
              .thenReturn(Mono.just(existingMovement));

      StepVerifier.create(movementService.save(newMovement, numAccountFake))
              .expectErrorMatches(e -> e instanceof EntityNotFoundException
                      && e.getMessage().contains("Movimiento ya existe"))
              .verify();

      verify(movementRepositoryPort).findByUuid(existingUuid);
      verify(accountRepositoryPort, never()).findByAccountNum(any());
    }

    @Test
    @DisplayName("should fail when account not found")
    void shouldFailWhenAccountNotFound() {
      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(new BigDecimal("100.00"))
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .expectErrorMatches(e -> e instanceof EntityNotFoundException
                      && e.getMessage().contains("Cuenta no encontrada"))
              .verify();

      verify(accountRepositoryPort).findByAccountNum(numAccountFake);
      verify(accountRepositoryPort, never()).saveOnly(any());
    }

    @Test
    @DisplayName("should fail when insufficient funds")
    void shouldFailWhenInsufficientFunds() {
      final var initialFunds = new BigDecimal("100.00");
      final var movementValue = new BigDecimal("-200.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .expectErrorMatches(e -> e instanceof InsufficientFundsException
                      && e.getMessage().contains("Saldo insuficiente"))
              .verify();

      verify(accountRepositoryPort, never()).saveOnly(any());
    }

    @Test
    @DisplayName("should generate UUID when not provided")
    void shouldGenerateUuidWhenNull() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("100.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(null)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(null))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).save(argThat(m -> m.getUuid() != null));
    }

    @Test
    @DisplayName("should set date to today when not provided")
    void shouldSetDateToTodayWhenNull() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("100.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(null)
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).save(argThat(m -> m.getDate() != null));
    }

    @Test
    @DisplayName("should correctly update account balance")
    void shouldUpdateAccountBalance() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("500.00");
      final var expectedBalance = new BigDecimal("1500.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());

      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).save(argThat(m ->
              m.getBalance().compareTo(expectedBalance) == 0
      ));
    }

    @Test
    @DisplayName("should classify deposit correctly")
    void shouldClassifyAsDeposit() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("100.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).save(argThat(m ->
              "Deposito".equals(m.getTypeMovement())
      ));
    }

    @Test
    @DisplayName("should classify withdrawal correctly")
    void shouldClassifyAsWithdrawal() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = new BigDecimal("-100.00");

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).save(argThat(m ->
              "Retiro".equals(m.getTypeMovement())
      ));
    }

    @Test
    @DisplayName("should handle zero value movement (edge case)")
    void shouldHandleZeroValueMovement() {
      final var initialFunds = new BigDecimal("1000.00");
      final var movementValue = BigDecimal.ZERO;

      final var account = Account.builder()
              .id(1L)
              .numAccount(numAccountFake)
              .initialFunds(initialFunds)
              .build();

      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(movementValue)
              .date(LocalDate.now())
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());
      when(accountRepositoryPort.findByAccountNum(numAccountFake))
              .thenReturn(Mono.just(account));
      when(accountRepositoryPort.saveOnly(any(Account.class)))
              .thenReturn(Mono.empty());
      when(movementRepositoryPort.save(any(Movement.class)))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.save(movement, numAccountFake))
              .verifyComplete();

      verify(movementRepositoryPort).save(any(Movement.class));
    }
  }

  @Nested
  @DisplayName("update Tests")
  class UpdateTests {
    private static final String movementUuidFake = "550e8400-e29b-41d4-a716-446655440000";

    @Test
    @DisplayName("should update existing movement")
    void shouldUpdateMovement() {
      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(new BigDecimal("100.00"))
              .typeMovement("Deposito")
              .build();

      final var existingMovement = Movement.builder()
              .uuid(movementUuidFake)
              .value(new BigDecimal("50.00"))
              .typeMovement("Retiro")
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.just(existingMovement));
      when(movementRepositoryPort.update(existingMovement))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.update(movement))
              .verifyComplete();

      verify(movementRepositoryPort).findByUuid(movementUuidFake);
      verify(movementRepositoryPort).update(any(Movement.class));
    }

    @Test
    @DisplayName("should fail when movement not found during update")
    void shouldFailWhenMovementNotFoundDuringUpdate() {
      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(new BigDecimal("100.00"))
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.update(movement))
              .expectErrorMatches(e -> e instanceof EntityNotFoundException
                      && e.getMessage().contains("movimiento no encontrado"))
              .verify();

      verify(movementRepositoryPort).findByUuid(movementUuidFake);
      verify(movementRepositoryPort, never()).update(any());
    }
  }

  @Nested
  @DisplayName("delete() Tests")
  class DeleteTests {
    private static final String movementUuidFake = "550e8400-e29b-41d4-a716-446655440000";

    @Test
    @DisplayName("should delete existing movement")
    void shouldDeleteMovement() {
      final var movement = Movement.builder()
              .uuid(movementUuidFake)
              .value(new BigDecimal("100.00"))
              .build();

      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.just(movement));
      when(movementRepositoryPort.delete(movementUuidFake))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.delete(movementUuidFake))
              .verifyComplete();

      verify(movementRepositoryPort).findByUuid(movementUuidFake);
      verify(movementRepositoryPort).delete(movementUuidFake);
    }

    @Test
    @DisplayName("should fail when movement not found during delete")
    void shouldFailWhenMovementNotFoundDuringDelete() {
      when(movementRepositoryPort.findByUuid(movementUuidFake))
              .thenReturn(Mono.empty());

      StepVerifier.create(movementService.delete(movementUuidFake))
              .expectErrorMatches(e -> e instanceof EntityNotFoundException
                      && e.getMessage().contains("movimiento no encontrado"))
              .verify();

      verify(movementRepositoryPort).findByUuid(movementUuidFake);
      verify(movementRepositoryPort, never()).delete(any());
    }
  }
}