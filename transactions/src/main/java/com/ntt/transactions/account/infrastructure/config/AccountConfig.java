package com.ntt.transactions.account.infrastructure.config;

import com.ntt.transactions.account.application.port.in.AccountDeleteUseCase;
import com.ntt.transactions.account.application.port.in.AccountSearchUseCase;
import com.ntt.transactions.account.infrastructure.in.messaging.AccountMessagingInputAdapter;
import com.ntt.transactions.account.infrastructure.in.messaging.mapper.AccountMessagingInputMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class AccountConfig {

  @Bean
  public AccountMessagingInputAdapter accountMessagingInputAdapter(
      AccountSearchUseCase accountSearchUseCase,
      AccountDeleteUseCase accountDeleteUseCase,
      AccountMessagingInputMapper accountMessagingInputMapper,
      ObjectMapper objectMapper
  ) {
    return new AccountMessagingInputAdapter(
        accountDeleteUseCase, accountSearchUseCase, accountMessagingInputMapper, objectMapper);
  }
}
