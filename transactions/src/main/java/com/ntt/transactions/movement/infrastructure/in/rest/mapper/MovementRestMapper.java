package com.ntt.transactions.movement.infrastructure.in.rest.mapper;

import com.ntt.transactions.movement.domain.model.Movement;
import com.ntt.transactions.movement.infrastructure.in.rest.model.MovementRequest;
import com.ntt.transactions.movement.infrastructure.in.rest.model.MovementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovementRestMapper {
  Movement toMovement(MovementRequest movementRequest);

  MovementResponse toMovementResponse(Movement movement);
}
