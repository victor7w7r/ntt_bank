package com.ntt.transactions.movement.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("movement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementEntity {
  @Id
  private Long id;

  private LocalDate date;

  @Column("type_movement")
  private String typeMovement;

  private BigDecimal value;
  private BigDecimal balance;
  private String uuid;

  @Column("account_movement")
  private Long accountMovement;
}