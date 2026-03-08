package com.ntt.transactions.movement.infrastructure.in.rest.model;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementRequest {

  @NotNull private LocalDate date;

  private String typeMovement;

  @NotNull private BigDecimal value;

  private BigDecimal balance;
  private String uuid;
}
