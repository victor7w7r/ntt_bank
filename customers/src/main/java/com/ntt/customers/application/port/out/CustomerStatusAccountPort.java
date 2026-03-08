package com.ntt.customers.application.port.out;

import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface CustomerStatusAccountPort {
  Flux<StatusAccountReceive> emitRequestStatusAccount(
      LocalDate startDate, LocalDate endDate, String idNumber, Customer customer);
}
