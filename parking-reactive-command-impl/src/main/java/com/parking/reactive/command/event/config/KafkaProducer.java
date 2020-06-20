package com.parking.reactive.command.event.config;

import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface KafkaProducer {

  void send(String topic, Object key, Object message);

  default void sendMessage(String topic, Object key, Object message, String messageString) {
    ListenableFuture<SendResult<Object, Object>> future = getKafkaTemplate().send(topic, key, messageString);
    future.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
      @Override
      public void onFailure(Throwable throwable) {
        getLogger().error("Cannot send message {}, Exception: {}", message, throwable.getMessage());
      }

      @Override
      public void onSuccess(SendResult<Object, Object> objectObjectSendResult) {
        getLogger().info("Message sent: {}", message);
      }
    });
  }

  Logger getLogger();

  KafkaTemplate<Object, Object> getKafkaTemplate();
}
