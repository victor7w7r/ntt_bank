package com.ntt.transactions.account.infrastructure.in.rest.model;

import com.ntt.transactions.movement.domain.model.Movement;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
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
