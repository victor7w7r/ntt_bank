package com.ntt.transactions.account.infrastructure.out.messaging;

import com.ntt.transactions.account.application.port.out.AccountMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class AccountMessagingOutputAdapter implements AccountMessagingPort {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public Mono<Long> sendIdReceiveRef(String idNumber) {
    return Mono.fromCallable(() -> {
        rabbitTemplate.setReplyTimeout(2000);
        Object response = rabbitTemplate.convertSendAndReceive("bank-ntt", "account.created", idNumber);
        return (Long) response;
      })
      .subscribeOn(Schedulers.boundedElastic());
  }
}
