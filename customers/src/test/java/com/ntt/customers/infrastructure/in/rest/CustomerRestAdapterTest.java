package com.ntt.customers.infrastructure.in.rest;

import com.ntt.customers.application.port.in.CustomerCreateUseCase;
import com.ntt.customers.application.port.in.CustomerDeleteUseCase;
import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.application.port.in.CustomerUpdateUseCase;
import com.ntt.customers.domain.exception.CustomerNotFoundException;
import com.ntt.customers.domain.model.Customer;
import com.ntt.customers.infrastructure.in.rest.mapper.CustomerRestMapper;
import com.ntt.customers.infrastructure.in.rest.model.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerRestAdapter.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerRestAdapterTest {

  private final Customer customer = Customer.builder()
          .id(1L)
          .name("Victor")
          .gender("Masculino")
          .age(20)
          .idNumber("1725082786")
          .address("Call Segovia y Raices")
          .phone("0984565509")
          .password("victorContrasena")
          .status(true)
          .build();
  private final CustomerResponse customerResponse = CustomerResponse.builder()
          .name("Victor")
          .gender("Masculino")
          .age(20)
          .idNumber("1725082786")
          .address("Call Segovia y Raices")
          .phone("0984565509")
          .password("victorContrasena")
          .status(true)
          .build();
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private CustomerSearchUseCase customerSearchUseCase;
  @MockitoBean
  private CustomerCreateUseCase customerCreateUseCase;
  @MockitoBean
  private CustomerUpdateUseCase customerUpdateUseCase;
  @MockitoBean
  private CustomerDeleteUseCase customerDeleteUseCase;
  @MockitoBean
  private CustomerRestMapper customerRestMapper;

  @Test
  public void clientControllerTest_getAllClientsReturnsListOfClients()
          throws Exception {
    when(customerSearchUseCase.findAll()).thenReturn(List.of(customer));
    when(customerRestMapper.toClientResponseList(any())).thenReturn(
            List.of(customerResponse)
    );
    mockMvc
            .perform(get("/api/v1/clients"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value(customerResponse));

    verify(customerSearchUseCase, times(1)).findAll();
  }

  @Test
  public void clientControllerTest_getStatusAccountReturnsList()
          throws Exception {
    when(
            customerSearchUseCase.requestStatusAccount(any(), any(), any())
    ).thenReturn(List.of());
    mockMvc
            .perform(
                    get("/api/v1/clients/reportes")
                            .param("fechaInicio", "2021-01-01")
                            .param("fechaFin", "2021-01-01")
                            .param("idNumber", "1725082786")
            )
            .andExpect(status().isOk());
    verify(customerSearchUseCase, times(1)).requestStatusAccount(
            any(),
            any(),
            any()
    );
  }

  @Test
  public void clientControllerTest_getStatusAccountReturnsBadRequest()
          throws Exception {
    when(customerSearchUseCase.requestStatusAccount(any(), any(), any())).thenThrow(
            new CustomerNotFoundException("ERROR: Cliente no encontrado")
    );
    mockMvc
            .perform(
                    get("/api/v1/clients/reportes")
                            .param("fechaInicio", "2021-01-01")
                            .param("fechaFin", "2021-01-01")
                            .param("idNumber", "1725082786")
            )
            .andExpect(status().isBadRequest());
    verify(customerSearchUseCase, times(1)).requestStatusAccount(
            any(),
            any(),
            any()
    );
  }

  @Test
  public void clientControllerTest_saveClientReturnsOk() throws Exception {
    doNothing().when(customerCreateUseCase).save(any());
    when(customerRestMapper.toClient(any())).thenReturn(customer);
    mockMvc
            .perform(
                    post("/api/v1/clients")
                            .content(
                                    "{\"name\":\"Victor Jimenez\"," +
                                            "\"gender\":\"masculino\"," +
                                            "\"age\":30," +
                                            "\"idNumber\":\"1725082786\"," +
                                            "\"address\":\"Amazonas y NNUU\"," +
                                            "\"phone\":\"0984565509\"," +
                                            "\"password\":\"ntttest\"}"
                            )
                            .contentType("application/json")
            )
            .andExpect(status().isCreated())
            .andExpect(
                    jsonPath("$.status").value("Client guardado exitosamente")
            );
    verify(customerCreateUseCase, times(1)).save(any());
  }

  @Test
  public void clientControllerTest_saveClientReturnsBadRequest()
          throws Exception {
    doThrow(new CustomerNotFoundException("ERROR: Client ya existe"))
            .when(customerCreateUseCase)
            .save(any());
    mockMvc
            .perform(
                    post("/api/v1/clients")
                            .content(
                                    "{\"name\":\"Victor Jimenez\"," +
                                            "\"gender\":\"masculino\"," +
                                            "\"age\":30," +
                                            "\"idNumber\":\"1725082786\"," +
                                            "\"address\":\"Amazonas y NNUU\"," +
                                            "\"phone\":\"0984565509\"," +
                                            "\"password\":\"ntttest\"}"
                            )
                            .contentType("application/json")
            )
            .andExpect(status().isBadRequest());
    verify(customerCreateUseCase, times(1)).save(any());
  }

  @Test
  public void clientControllerTest_updateClientReturnsOk()
          throws Exception {
    doNothing().when(customerUpdateUseCase).update(any());
    mockMvc
            .perform(
                    put("/api/v1/clients")
                            .content(
                                    "{\"name\":\"Victor Jimenez\"," +
                                            "\"gender\":\"masculino\"," +
                                            "\"age\":30," +
                                            "\"idNumber\":\"1725082786\"," +
                                            "\"address\":\"Amazonas y NNUU\"," +
                                            "\"phone\":\"0984565509\"," +
                                            "\"password\":\"ntttest\"}"
                            )
                            .contentType("application/json")
            )
            .andExpect(status().isAccepted())
            .andExpect(
                    jsonPath("$.status").value("Client actualizado exitosamente")
            );
    verify(customerUpdateUseCase, times(1)).update(any());
  }

  @Test
  public void clientControllerTest_updateClientReturnsBadRequest()
          throws Exception {
    doThrow(new CustomerNotFoundException("ERROR: Client no encontrado"))
            .when(customerUpdateUseCase)
            .update(any());
    mockMvc
            .perform(
                    put("/api/v1/clients")
                            .content(
                                    "{\"name\":\"Victor Jimenez\"," +
                                            "\"gender\":\"masculino\"," +
                                            "\"age\":30," +
                                            "\"idNumber\":\"1725082786\"," +
                                            "\"address\":\"Amazonas y NNUU\"," +
                                            "\"phone\":\"0984565509\"," +
                                            "\"password\":\"ntttest\"}"
                            )
                            .contentType("application/json")
            )
            .andExpect(status().isBadRequest());
    verify(customerUpdateUseCase, times(1)).update(any());
  }

  @Test
  public void clientControllerTest_deleteClientReturnsOk()
          throws Exception {
    doNothing().when(customerDeleteUseCase).delete(any());
    mockMvc
            .perform(delete("/api/v1/clients/1725082786"))
            .andExpect(status().isAccepted())
            .andExpect(
                    jsonPath("$.status").value("Client eliminado exitosamente")
            );
    verify(customerDeleteUseCase, times(1)).delete(any());
  }

  @Test
  public void clientControllerTest_deleteClientReturnsBadRequest()
          throws Exception {
    doThrow(new CustomerNotFoundException("ERROR: Client no encontrado"))
            .when(customerDeleteUseCase)
            .delete(any());
    mockMvc
            .perform(delete("/api/v1/clients/1725082786"))
            .andExpect(status().isBadRequest());
    verify(customerDeleteUseCase, times(1)).delete(any());
  }
}
