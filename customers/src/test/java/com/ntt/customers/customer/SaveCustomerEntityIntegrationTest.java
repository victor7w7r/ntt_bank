package com.ntt.customers.customer;

import com.intuit.karate.junit5.Karate;
import com.ntt.customers.application.port.in.CustomerCreateUseCase;
import com.ntt.customers.application.port.in.CustomerDeleteUseCase;
import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import com.ntt.customers.application.port.in.CustomerUpdateUseCase;
import com.ntt.customers.domain.model.Customer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SaveCustomerEntityIntegrationTest {

  @LocalServerPort
  private int port;

  @MockitoBean
  private CustomerCreateUseCase customerCreateUseCase;

  @MockitoBean
  private CustomerSearchUseCase customerSearchUseCase;

  @MockitoBean
  private CustomerUpdateUseCase customerUpdateUseCase;

  @MockitoBean
  private CustomerDeleteUseCase customerDeleteUseCase;

  @Karate.Test
  Karate testSaveCustomer() {
    Customer victor = Customer.builder()
            .id(1L)
            .name("Victor")
            .gender("Masculino")
            .age(20)
            .idNumber(1725082786L)
            .address("Call Segovia y Raices")
            .phone("0984565509")
            .password("victorContrasena")
            .status(true)
            .build();

    when(customerCreateUseCase.save(any(Customer.class)))
            .thenReturn(Mono.just(victor).then());

    when(customerSearchUseCase.findAll())
            .thenReturn(Flux.just(victor));

    return Karate.run("customer")
            .relativeTo(getClass())
            .systemProperty("baseUrl", "http://localhost:" + port);
  }
}
