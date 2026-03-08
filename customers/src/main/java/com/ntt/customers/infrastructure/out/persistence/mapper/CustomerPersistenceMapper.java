package com.ntt.customers.infrastructure.out.persistence.mapper;

import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.infrastructure.out.persistence.entity.CustomerEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
  CustomerEntity toCustomerEntity(Customer customer);
  Customer toCustomer(CustomerEntity entity);

  @Mapping(target = "name", expression = "java(source.getName())")
  @Mapping(target = "gender", expression = "java(source.getGender())")
  @Mapping(target = "age", expression = "java(source.getAge())")
  @Mapping(target = "idNumber", expression = "java(source.getIdNumber())")
  @Mapping(target = "address", expression = "java(source.getAddress())")
  @Mapping(target = "phone", expression = "java(source.getPhone())")
  void update(Customer source, @MappingTarget CustomerEntity target);
}
