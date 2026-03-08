package com.ntt.transactions.account.infrastructure.in.messaging.entity;

import java.math.BigDecimal;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountReceiveRes {

  private String date;
  private String customer;
  private Long numAccount;
  private String accountType;
  private String typeMovement;
  private BigDecimal movement;
  private BigDecimal balance;
  private BigDecimal availableFunds;
}
