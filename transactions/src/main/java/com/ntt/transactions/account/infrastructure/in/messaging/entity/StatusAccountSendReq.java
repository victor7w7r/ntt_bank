package com.ntt.transactions.account.infrastructure.in.messaging.entity;

import java.time.LocalDate;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountSendReq {

  private LocalDate startDate;
  private LocalDate endDate;
  private Long customerRef;
  private String nameCustomer;
}
