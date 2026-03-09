package com.ntt.transactions.account.application.port.out;

import reactor.core.publisher.Mono;

public interface AccountMessagingPort {
  Mono<Long> sendIdReceiveRef(Long idNumber);
}
