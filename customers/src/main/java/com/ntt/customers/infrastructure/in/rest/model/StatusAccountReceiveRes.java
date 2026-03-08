package com.ntt.customers.infrastructure.in.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
