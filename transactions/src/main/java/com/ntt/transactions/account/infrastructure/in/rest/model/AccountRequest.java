package com.ntt.transactions.account.infrastructure.in.rest.model;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
  @NotNull private Long numAccount;
  @NotNull private String accountType;
  @NotNull private BigDecimal initialFunds;
  private Long customerRef;
  private Boolean status;
}
