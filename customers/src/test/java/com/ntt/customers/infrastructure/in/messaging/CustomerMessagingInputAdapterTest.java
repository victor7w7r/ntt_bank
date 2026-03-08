package com.ntt.customers.infrastructure.in.messaging;

import com.ntt.customers.application.port.in.CustomerSearchUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerMessagingInputAdapterTest {

  @Mock
  private CustomerSearchUseCase clientSendUseCase;

  @InjectMocks
  private CustomerMessagingInputAdapter customerMessagingInputAdapter;

  @Test
  public void clientMessagingInputAdapterTest_sendClientRefReturnsClientId() {
    customerMessagingInputAdapter.sendClientRef("1725082786");
    verify(clientSendUseCase, times(1)).sendCustomerRef("1725082786");
  }
}
