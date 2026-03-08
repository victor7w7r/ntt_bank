package com.ntt.customers.infrastructure.out.messaging.mapper;

import com.ntt.customers.infrastructure.out.messaging.entity.StatusAccountSendReq;
import java.time.LocalDate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMessagingOutputMapper {
  StatusAccountSendReq toStatusAccountSendReq(
      LocalDate startDate, LocalDate endDate, Long customerRef, String customerName);
}
