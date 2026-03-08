package com.ntt.customers.infrastructure.in.rest.model;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOperationResponse {
  private String status;
  private String message;
}
