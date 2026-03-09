package com.ntt.customers.infrastructure.out.messaging;

import com.ntt.customers.application.port.out.CustomerStatusAccountPort;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import com.ntt.customers.infrastructure.out.messaging.mapper.CustomerMessagingOutputMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerMessagingOutputAdapter implements CustomerStatusAccountPort {

  private final RabbitTemplate rabbitTemplate;
  private final CustomerMessagingOutputMapper customerMessagingOutputMapper;
  private final ObjectMapper objectMapper;

  private String converter(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return date.format(formatter);
  }

  @Override
  public Flux<StatusAccountReceive> requestStatusAccount(
          LocalDate startDate,
          LocalDate endDate,
          Long idNumber,
          Customer customer
  ) {
    return Flux.defer(() -> {
              try {
                rabbitTemplate.setReplyTimeout(8000);
                String jsonReq = objectMapper.writeValueAsString(
                        customerMessagingOutputMapper.toStatusAccountSendReq(
                                converter(startDate), converter(endDate), customer.getId(), customer.getName())
                );
                log.info("Sending message: {}", jsonReq);
                Object response = rabbitTemplate.convertSendAndReceive(
                        "bank-ntt",
                        "account.status.requested",
                        jsonReq
                );
                log.info("Received response: {}", response);
                if (response == null) {
                  return Flux.error(new RuntimeException("Timeout: Error with communication"));
                }
                final var listResponse = objectMapper.readValue(
                        response.toString(),
                        new TypeReference<List<StatusAccountReceive>>() {}
                );
                return Flux.fromIterable(listResponse);

              } catch (Exception e) {
                return Flux.error(new RuntimeException("Error with communication", e));
              }
            })
            .subscribeOn(Schedulers.boundedElastic());
  }
}