package com.ntt.customers.infrastructure.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {
  @Id
  private Long id;
  private String name;
  private String gender;
  private Integer age;
  private String idNumber;
  private String address;
  private String phone;
}
