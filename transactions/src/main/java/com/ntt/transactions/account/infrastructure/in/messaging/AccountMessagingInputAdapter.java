package com.ntt.transactions.account.infrastructure.in.messaging;

import com.ntt.transactions.account.application.port.in.AccountDeleteUseCase;
import com.ntt.transactions.account.application.port.in.AccountSearchUseCase;
import com.ntt.transactions.account.infrastructure.in.messaging.entity.StatusAccountReceiveRes;
import com.ntt.transactions.account.infrastructure.in.messaging.entity.StatusAccountSendReq;
import com.ntt.transactions.account.infrastructure.in.messaging.mapper.AccountMessagingInputMapper;
import com.ntt.transactions.common.config.BrokerConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AccountMessagingInputAdapter {

  private final AccountDeleteUseCase accountDeleteUseCase;
  private final AccountSearchUseCase accountSearchUseCase;
  private final AccountMessagingInputMapper accountMessagingInputMapper;
  private final ObjectMapper objectMapper;

  @RabbitListener(queues = BrokerConfig.CUSTOMER_ERASE_QUEUE)
  public Long deleteAccountPerQueue(String message) {
    Long clientRef = Long.parseLong(message);
    return accountDeleteUseCase.deleteByCustomerRef(clientRef).block();
  }

  @RabbitListener(queues = BrokerConfig.ACCOUNT_STATUS_QUEUE)
  public String requestEstadoAccount(String message) throws Exception {
    log.info("Received message: {}", message);
    StatusAccountSendReq req = objectMapper.readValue(message, StatusAccountSendReq.class);
    List<StatusAccountReceiveRes> result = accountSearchUseCase
            .requestStatusAccount(accountMessagingInputMapper.toStatusAccountSend(req))
            .map(accountMessagingInputMapper::toStatusAccountReceiveRes)
            .collectList()
            .block();
    final var res = objectMapper.writeValueAsString(result);
    log.info("Sending response {}",  res);
    return res;
  }
}
