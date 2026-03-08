package com.ntt.transactions.account.infrastructure.in.messaging.mapper;

import com.ntt.transactions.account.domain.model.StatusAccountReceive;
import com.ntt.transactions.account.domain.model.StatusAccountSend;
import com.ntt.transactions.account.infrastructure.in.messaging.entity.StatusAccountReceiveRes;
import com.ntt.transactions.account.infrastructure.in.messaging.entity.StatusAccountSendReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMessagingInputMapper {
  StatusAccountReceiveRes toStatusAccountReceiveRes(
          StatusAccountReceive statusAccountReceive
  );

  StatusAccountSend toStatusAccountSend(
          StatusAccountSendReq statusAccountSendReq
  );
}
