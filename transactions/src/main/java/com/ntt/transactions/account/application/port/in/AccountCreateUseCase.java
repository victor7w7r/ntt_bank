package com.ntt.transactions.account.application.port.in;

import com.ntt.transactions.account.domain.model.Account;
import reactor.core.publisher.Mono;

public interface AccountCreateUseCase {
  Mono<Void> save(Account account, Long idNumber);
}
