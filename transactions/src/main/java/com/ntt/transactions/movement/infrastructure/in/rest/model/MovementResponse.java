package com.ntt.transactions.movement.infrastructure.in.rest.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementResponse {
  private LocalDate date;
  private String typeMovement;
  private BigDecimal value;
  private BigDecimal balance;
  private String uuid;
}
