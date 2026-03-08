package com.ntt.transactions.account.domain.model;

import java.time.LocalDate;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountSend {

  private LocalDate startDate;
  private LocalDate endDate;
  private Long customerRef;
  private String nameCustomer;
}
