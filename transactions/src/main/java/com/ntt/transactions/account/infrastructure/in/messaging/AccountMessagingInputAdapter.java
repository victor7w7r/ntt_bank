package com.ntt.transactions.account.infrastructure.in.messaging;

import com.ntt.transactions.account.application.port.in.AccountDeleteUseCase;
import com.ntt.transactions.account.application.port.in.AccountSearchUseCase;
import com.ntt.transactions.account.infrastructure.in.messaging.entity.StatusAccountReceiveRes;
import com.ntt.transactions.account.infrastructure.in.messaging.entity.StatusAccountSendReq;
import com.ntt.transactions.account.infrastructure.in.messaging.mapper.AccountMessagingInputMapper;
import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class AccountMessagingInputAdapter {

  private final AccountDeleteUseCase accountDeleteUseCase;
  private final AccountSearchUseCase accountSearchUseCase;
  private final AccountMessagingInputMapper accountMessagingInputMapper;

  @Bean
  public Function<Mono<Long>, Mono<Long>> deleteAccountReceiver() {
    return flux -> flux.flatMap(accountDeleteUseCase::deleteByCustomerRef);
  }

  @Bean
  public Function<Flux<StatusAccountSendReq>, Flux<StatusAccountReceiveRes>> statusAccountReceiver() {
    return flux -> flux.flatMap(req ->
       accountSearchUseCase.requestStatusAccount(accountMessagingInputMapper.toStatusAccountSend(req))
              .map(accountMessagingInputMapper::toStatusAccountReceiveRes)
    );
  }
}
