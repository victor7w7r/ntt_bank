package com.ntt.customers.infrastructure.out.messaging;

import com.ntt.customers.application.port.out.CustomerStatusAccountPort;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import com.ntt.customers.infrastructure.out.messaging.mapper.CustomerMessagingOutputMapper;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerMessagingOutputAdapter implements CustomerStatusAccountPort {

  private final RabbitTemplate rabbitTemplate;
  private final CustomerMessagingOutputMapper customerMessagingOutputMapper;
  private final ObjectMapper objectMapper;

  @Override
  public Flux<StatusAccountReceive> emitRequestStatusAccount(
          LocalDate startDate,
          LocalDate endDate,
          String idNumber,
          Customer customer
  ) {
    return Mono.fromCallable(() -> {
              rabbitTemplate.setReplyTimeout(8000);
              Object response = rabbitTemplate.convertSendAndReceive(
                      "bank-ntt",
                      "account_client_queue",
                      customerMessagingOutputMapper.toStatusAccountSendReq(
                              startDate,
                              endDate,
                              customer.getId(),
                              customer.getName()
                      )
              );

              if (response == null) {
                return List.<StatusAccountReceive>of();
              }

              return objectMapper.convertValue(
                      response,
                      new TypeReference<List<StatusAccountReceive>>() {}
              );
            })
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapIterable(list -> list)
            .log();
  }
}
