package com.ntt.transactions.account.infrastructure.in.messaging.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountSendReq {

  private String startDate;
  private String endDate;
  private Long customerRef;
  private String customerName;
}
