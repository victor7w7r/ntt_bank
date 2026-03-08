package com.ntt.customers.infrastructure.out.persistence.mapper;

import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.infrastructure.out.persistence.entity.CustomerEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
  CustomerEntity toCustomerEntity(Customer customer);
  Customer toCustomer(CustomerEntity entity);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "age", target = "age")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "phone", target = "phone")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "status", target = "status")
  void update(Customer source, @MappingTarget CustomerEntity target);
}
