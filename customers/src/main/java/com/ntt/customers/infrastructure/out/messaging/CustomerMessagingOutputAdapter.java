package com.ntt.customers.infrastructure.out.messaging;

import com.ntt.customers.application.port.out.CustomerStatusAccountPort;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import com.ntt.customers.infrastructure.out.messaging.mapper.CustomerMessagingOutputMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerMessagingOutputAdapter implements CustomerStatusAccountPort {

  private final CustomerMessagingOutputMapper customerMessagingOutputMapper;
  private final CustomerMessagingOutputMapper mapper;
  private final StreamBridge streamBridge;
  private final Sinks.Many<StatusAccountReceive> responseSink =
    Sinks.many().multicast().onBackpressureBuffer();

  @Override
  public Flux<StatusAccountReceive> requestStatusAccount(
          LocalDate startDate,
          LocalDate endDate,
          String idNumber,
          Customer customer
  ) {
    return Mono.fromRunnable(() -> {
        log.info("Enviando petición de estado de cuenta para: {}", customer.getName());
        final var request = mapper.toStatusAccountSendReq(
                startDate, endDate, customer.getId(), customer.getName()
        );
        streamBridge.send("customerRequest-out-0", request);
      })
      .subscribeOn(Schedulers.boundedElastic())
      .thenMany(responseSink.asFlux())
      .timeout(Duration.ofSeconds(8))
      .doOnError(e -> log.error("Error al obtener el estado de cuenta: {}", e.getMessage()));
  }
}
