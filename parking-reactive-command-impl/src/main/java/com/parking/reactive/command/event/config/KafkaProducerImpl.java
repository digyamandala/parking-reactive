package com.parking.reactive.command.event.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducerImpl implements KafkaProducer {

  private final KafkaTemplate kafkaTemplate;

  private final ObjectMapper objectMapper;

  public KafkaProducerImpl(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public void send(String topic, Object key, Object message) {
    try {
      String messageString = objectMapper.writeValueAsString(message);
      sendMessage(topic, key, message, messageString);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public KafkaTemplate<Object, Object> getKafkaTemplate() {
    return kafkaTemplate;
  }
}
