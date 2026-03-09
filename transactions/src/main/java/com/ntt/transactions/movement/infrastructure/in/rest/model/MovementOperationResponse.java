package com.ntt.transactions.movement.infrastructure.in.rest.model;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MovementOperationResponse {
  private String message;
}
