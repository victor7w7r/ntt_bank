package com.ntt.transactions.account.infrastructure.out.persistence.mapper;

import com.ntt.transactions.account.domain.model.Account;
import com.ntt.transactions.account.infrastructure.out.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountPersistenceMapper {
  AccountEntity toAccountEntity(Account account);
  Account toAccount(AccountEntity value);

  void update(Account source, @MappingTarget AccountEntity target);

  @Mapping(target = "numAccount", ignore = true)
  void updateWithoutNumAccount(
          Account source,
          @MappingTarget AccountEntity target
  );
}
