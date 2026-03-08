package com.ntt.customers.infrastructure.in.rest.mapper;

import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.domain.model.StatusAccountReceive;
import com.ntt.customers.infrastructure.in.rest.model.CustomerRequest;
import com.ntt.customers.infrastructure.in.rest.model.CustomerResponse;
import com.ntt.customers.infrastructure.in.rest.model.StatusAccountReceiveRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerRestMapper {
  Customer toCustomer(CustomerRequest customerRequest);
  CustomerResponse toCustomerResponse(Customer customer);

  StatusAccountReceiveRes toStatusAccountReceiveRes(
          StatusAccountReceive statusAccountReceive
  );

  List<StatusAccountReceiveRes> toStatusAccountReceiveResList(
          List<StatusAccountReceive> statusAccountReceiveList
  );
}
