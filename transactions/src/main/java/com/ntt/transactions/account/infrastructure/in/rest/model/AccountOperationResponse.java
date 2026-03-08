package com.ntt.transactions.account.infrastructure.in.rest.model;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationResponse {
  private String status;
  private String message;
}
