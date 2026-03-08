package com.ntt.customers.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  private Long id;
  private String name;
  private String gender;
  private Integer age;
  private String idNumber;
  private String address;
  private String phone;
  private String password;
  private Boolean status;
}
