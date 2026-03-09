package com.ntt.customers.domain.model;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
  private Long id;
  private String name;
  private String gender;
  private Integer age;
  private Long idNumber;
  private String address;
  private String phone;
  private String password;
  private Boolean status;
}
