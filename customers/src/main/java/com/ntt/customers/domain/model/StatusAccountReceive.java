package com.ntt.customers.domain.model;

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
  private String movementType;
  private BigDecimal movement;
  private BigDecimal balance;
}
