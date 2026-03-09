package com.ntt.transactions.account.infrastructure.out.messaging;

import com.ntt.transactions.account.application.port.out.AccountMessagingPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountMessagingOutputAdapter implements AccountMessagingPort {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public Mono<Long> sendIdReceiveRef(Long idNumber) {
    return Mono.fromCallable(
            () -> {
              Object response =
                  rabbitTemplate.convertSendAndReceive(
                      "bank-ntt", "account.status.requested", idNumber);

              if (response == null) {
                throw new RuntimeException("Customers service timeout");
              }

              log.info("The number is {}", response);

              if (response instanceof Number) {
                return ((Number) response).longValue();
              }
              return Long.parseLong(response.toString());
            })
        .flatMap(res -> res == 0L ? Mono.empty() : Mono.just(res))
        .subscribeOn(Schedulers.boundedElastic());
  }
}
