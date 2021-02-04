package com.bajdas.rest_shop.notification;

import com.bajdas.rest_shop.model.ClientTransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationSpawnerTest {

  private static final String SAMPLE_TOPIC_ARN = "sampleTopicArn";
  private static final ClientTransactionDto SAMPLE_TRANSACTION_DTO = ClientTransactionDto.builder()
      .transactionId(1L)
      .totalPrice(BigDecimal.ONE)
      .build();
  @Mock
  private SnsClient snsClientMock;
  @Captor
  private ArgumentCaptor<PublishRequest> snsRequestCaptor;
  private NotificationSpawner spawner;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    spawner = new NotificationSpawner(snsClientMock, SAMPLE_TOPIC_ARN);
  }

  @Test
  void shouldSendMessageWhenNotifiedByStatusTransactionCompleted() {
    //when
    spawner.update(Status.TRANSACTION_COMPLETED, SAMPLE_TRANSACTION_DTO);

    //then
    verify(snsClientMock).publish(any(PublishRequest.class));
  }

  @Test
  void shouldSendMessageWhenNotifiedByNotRelevantStatus() {
    //when
    spawner.update(null, SAMPLE_TRANSACTION_DTO);

    //then
    verify(snsClientMock, never()).publish(any(PublishRequest.class));
  }

  @Test
  void shouldSendMessageToSNS() {
    //when
    spawner.prepareAndSendMessage(SAMPLE_TRANSACTION_DTO);

    //then
    verify(snsClientMock).publish(snsRequestCaptor.capture());
    var actualSnsRequest = snsRequestCaptor.getValue();
    assertEquals(SAMPLE_TRANSACTION_DTO.toString(), actualSnsRequest.message());
    assertEquals(SAMPLE_TOPIC_ARN, actualSnsRequest.topicArn());
  }

  @Test
  void shouldThrowExceptionWhenSendingMessageToSNSFailed() {
    //given
    when(snsClientMock.publish(any(PublishRequest.class))).thenThrow(RuntimeException.class);

    //when
    assertThrows(SnSSendingException.class, () -> spawner.prepareAndSendMessage(SAMPLE_TRANSACTION_DTO));
  }

}