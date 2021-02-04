package com.bajdas.rest_shop.notification;

import com.bajdas.rest_shop.model.ClientTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Component
@Slf4j
public class NotificationSpawner implements Observer {

  private SnsClient snsClient;
  private String topicArn;

  @Autowired
  NotificationSpawner(SnsClient snsClient, @Value("${sns.topic.arn}") String topicArn) {
    this.snsClient = snsClient;
    this.topicArn = topicArn;
  }


  @Override
  public void update(Status status, ClientTransactionDto transaction) {
    if (status == Status.TRANSACTION_COMPLETED) {
      try {
        prepareAndSendMessage(transaction);
        log.info("Notification about transaction with id {} sent.", transaction.getTransactionId());
      } catch (SnSSendingException e) {
        log.error("SNS sending exception", e);
      }
    }
  }

  void prepareAndSendMessage(ClientTransactionDto transactionDto) {
    var request = PublishRequest.builder()
        .topicArn(topicArn)
        .message(transactionDto.toString())
        .build();
    try {
      snsClient.publish(request);
    } catch (Exception e) {
      throw new SnSSendingException();
    }
  }
}
