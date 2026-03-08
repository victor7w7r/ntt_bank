package com.ntt.transactions.movement.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

  private LocalDate date;
  private String typeMovement;
  private BigDecimal value;
  private BigDecimal balance;
  private String uuid;
  private String accountMovement;
}
