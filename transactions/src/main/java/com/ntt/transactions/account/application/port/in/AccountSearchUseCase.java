package com.ntt.transactions.account.application.port.in;

import com.ntt.transactions.account.domain.model.Account;
import com.ntt.transactions.account.domain.model.StatusAccountReceive;
import com.ntt.transactions.account.domain.model.StatusAccountSend;
import reactor.core.publisher.Flux;

public interface AccountSearchUseCase {
  Flux<Account> findAll();
  Flux<StatusAccountReceive> requestStatusAccount(StatusAccountSend req);
}
