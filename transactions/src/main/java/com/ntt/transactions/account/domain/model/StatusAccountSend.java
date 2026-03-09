package com.ntt.transactions.account.domain.model;

import java.time.LocalDate;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountSend {

  private String startDate;
  private String endDate;
  private Long customerRef;
  private String customerName;
}
