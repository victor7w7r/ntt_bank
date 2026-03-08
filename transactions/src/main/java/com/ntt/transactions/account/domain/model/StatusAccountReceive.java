package com.ntt.transactions.account.domain.model;

import java.math.BigDecimal;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountReceive {

  private String date;
  private String customer;
  private Long numAccount;
  private String accountType;
  private String typeMovement;
  private BigDecimal movementQuantity;
  private BigDecimal balance;
  private BigDecimal actualBalance;
}
