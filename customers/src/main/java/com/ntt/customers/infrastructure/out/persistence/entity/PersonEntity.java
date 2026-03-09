package com.ntt.customers.infrastructure.out.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;

@Data
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {
  private String name;
  private String gender;
  private Integer age;

  @Column("id_number")
  private Long idNumber;
  private String address;
  private String phone;
}
