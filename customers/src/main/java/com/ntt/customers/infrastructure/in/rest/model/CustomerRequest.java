package com.ntt.customers.infrastructure.in.rest.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

  @NotBlank(message = "El nombre no puede estar vacío")
  private String name;

  @NotBlank(message = "El género no puede estar vacío")
  private String gender;

  @NotNull
  @Min(1)
  private Integer age;

  @NotBlank(message = "La identificación no puede estar vacía")
  private String idNumber;

  @NotBlank(message = "La dirección no puede estar vacía")
  private String address;

  @NotBlank(message = "El teléfono no puede estar vacío")
  private String phone;

  @NotBlank(message = "La contraseña no puede estar vacía")
  private String password;

  private Boolean status;
}
