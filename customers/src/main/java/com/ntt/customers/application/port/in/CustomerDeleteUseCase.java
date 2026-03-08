package com.ntt.customers.application.port.in;

import reactor.core.publisher.Mono;

public interface CustomerDeleteUseCase {
  Mono<Void> delete(String idNumber);
}
