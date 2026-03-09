package com.ntt.transactions.account.application.port.in;

import reactor.core.publisher.Mono;

public interface AccountDeleteUseCase {
  Mono<Void> delete(Long numAccount);
  Mono<Long> deleteByCustomerRef(Long customerRef);
}
