package com.ntt.transactions.account.infrastructure.out.persistence.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("account")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
  @Id private Long id;

  @Column("num_account")
  private Long numAccount;

  @Column("account_type")
  private String accountType;

  @Column("initial_funds")
  private BigDecimal initialFunds;

  private Boolean status;

  @Column("customer_ref")
  private Long customerRef;
}
