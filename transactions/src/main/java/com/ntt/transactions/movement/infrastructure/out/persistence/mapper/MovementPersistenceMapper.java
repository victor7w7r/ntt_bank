package com.ntt.transactions.movement.infrastructure.out.persistence.mapper;

import com.ntt.transactions.movement.domain.model.Movement;
import com.ntt.transactions.movement.infrastructure.out.persistence.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovementPersistenceMapper {
  Movement toMovement(MovementEntity movementEntity);
  MovementEntity toMovementEntity(Movement movement);

  @Mapping(target = "uuid", ignore = true)
  void update(Movement source, @MappingTarget MovementEntity target);
}
