package com.ntt.transactions.account.infrastructure.out.persistence.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
  @Id
  private Long id;

  @Column("num_account")
  private Long numAccount;

  private String accountType;
  private BigDecimal initialFunds;
  private Boolean status;
  private Long customerRef;
}