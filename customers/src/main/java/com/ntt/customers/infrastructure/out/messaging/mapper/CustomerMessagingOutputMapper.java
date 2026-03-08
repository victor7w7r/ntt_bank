package com.ntt.customers.infrastructure.out.messaging.mapper;

import com.ntt.customers.infrastructure.out.messaging.entity.StatusAccountSendReq;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface CustomerMessagingOutputMapper {
  StatusAccountSendReq toStatusAccountSendReq(
          LocalDate startDate,
          LocalDate endDate,
          Long customerRef,
          String customerName
  );
}
