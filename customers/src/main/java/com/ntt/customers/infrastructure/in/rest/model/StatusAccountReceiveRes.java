package com.ntt.customers.infrastructure.in.rest.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountReceiveRes {

  private String date;
  private String customer;
  private Long numAccount;
  private String accountType;
  private String transactionType;
  private BigDecimal transaction;
  private BigDecimal availableFunds;
}
