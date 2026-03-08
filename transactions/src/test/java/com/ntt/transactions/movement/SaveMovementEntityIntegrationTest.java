package com.ntt.transactions.movement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ntt.transactions.account.domain.model.Account;
import com.ntt.transactions.account.infrastructure.out.persistence.AccountRepositoryPersistenceAdapter;
import com.ntt.transactions.movement.application.service.MovementService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SaveMovementEntityIntegrationTest {

  private static final String numAccount = "5578664578";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MovementService transactionService;

  @Autowired
  private AccountRepositoryPersistenceAdapter accountPersistenceAdapter;

  @Test
  void saveTransactionTestAndVerifyValor() throws Exception {
    accountPersistenceAdapter.saveOnly(
            Account.builder()
                    .numAccount(Long.valueOf(numAccount))
                    .accountType("Ahorros")
                    .initialFunds(new java.math.BigDecimal(500))
                    .status(true)
                    .build()
    );

    mockMvc.perform(
            post("/transactions/{numAccount}", numAccount).content(
                    """
                            {
                              "balance": 1000
                            }
                            """
            )
    );

    final var transaction = transactionService.findAll().getFirst();

    assertEquals(BigDecimal.valueOf(1500L), transaction.getBalance());
  }
}
