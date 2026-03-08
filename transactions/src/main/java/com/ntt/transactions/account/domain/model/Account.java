package com.ntt.transactions.account.domain.model;

import com.ntt.transactions.movement.domain.model.Movement;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  private Long id;
  private Long numAccount;
  private String accountType;
  private BigDecimal initialFunds;
  private Long customerRef;
  private Boolean status;
  private List<Movement> movements;
}
