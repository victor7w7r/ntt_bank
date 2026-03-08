package com.ntt.transactions.account.infrastructure.in.rest.model;

import com.ntt.transactions.movement.domain.model.Movement;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

  private Long numAccount;
  private String accountType;
  private BigDecimal initialFunds;
  private Boolean status;
  private List<Movement> movementEntities;
}
